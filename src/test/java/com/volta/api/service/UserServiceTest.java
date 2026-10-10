package com.volta.api.service;

import com.volta.api.database.entity.Users;
import com.volta.api.database.repository.CompanyRepository;
import com.volta.api.database.repository.RoleRepository;
import com.volta.api.database.repository.UserRepository;
import com.volta.api.dto.user.UserRoleUpdateRequestDTO;
import com.volta.api.dto.user.UserResponseDTO;
import com.volta.api.enums.RoleType;
import com.volta.api.exception.BusinessRuleException;
import com.volta.api.exception.ConflictException;
import com.volta.api.exception.ResourceNotFoundException;
import com.volta.api.mapper.UserMapper;
import com.volta.api.security.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static com.volta.api.support.UserFixtures.authenticatedUser;
import static com.volta.api.support.UserFixtures.company;
import static com.volta.api.support.UserFixtures.role;
import static com.volta.api.support.UserFixtures.userRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService service;

    private final UUID companyId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        service = new UserService(userRepository, companyRepository, roleRepository,
                passwordEncoder, new UserMapper());
    }

    @Nested
    @DisplayName("register")
    class Register {

        @Test
        @DisplayName("e-mail já cadastrado lança conflito e não salva")
        void register_emailAlreadyRegistered_throwsConflictAndDoesNotSave() {
            when(userRepository.findByEmail("ana@volta.com")).thenReturn(Optional.of(new Users()));

            assertThatThrownBy(() -> service.register(userRequest(companyId).build()))
                    .isInstanceOf(ConflictException.class)
                    .hasMessage("Email already registered");

            verify(userRepository, never()).save(any());
            verifyNoInteractions(companyRepository);
        }

        @Test
        @DisplayName("empresa inexistente lança not found")
        void register_missingCompany_throwsNotFound() {
            when(userRepository.findByEmail("ana@volta.com")).thenReturn(Optional.empty());
            when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.register(userRequest(companyId).build()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Company not found");

            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("dados válidos salvam com senha criptografada e papel EMPLOYEE")
        void register_validData_savesWithEncodedPasswordAndEmployeeRole() {
            UUID newId = UUID.randomUUID();
            when(userRepository.findByEmail("ana@volta.com")).thenReturn(Optional.empty());
            when(companyRepository.findById(companyId)).thenReturn(Optional.of(company(companyId)));
            when(roleRepository.findByType("EMPLOYEE")).thenReturn(Optional.of(role("EMPLOYEE")));
            when(passwordEncoder.encode("password1234")).thenReturn("bcrypt-hash");
            when(userRepository.save(any(Users.class))).thenAnswer(invocation -> {
                Users saved = invocation.getArgument(0);
                saved.setId(newId);
                return saved;
            });

            UserResponseDTO response = service.register(userRequest(companyId).build());

            assertThat(response.id()).isEqualTo(newId);
            assertThat(response.email()).isEqualTo("ana@volta.com");
            assertThat(response.role()).isEqualTo("EMPLOYEE");
            assertThat(response.companyId()).isEqualTo(companyId);

            ArgumentCaptor<Users> captor = ArgumentCaptor.forClass(Users.class);
            verify(userRepository).save(captor.capture());
            assertThat(captor.getValue().getPasswordHash()).isEqualTo("bcrypt-hash");
        }
    }

    @Nested
    @DisplayName("updateRole")
    class UpdateRole {

        @Test
        @DisplayName("alterar o próprio papel lança regra de negócio")
        void updateRole_ownRole_throwsBusinessRule() {
            UUID myId = UUID.randomUUID();
            AuthenticatedUser author = authenticatedUser(myId, companyId, "ADMIN");

            assertThatThrownBy(() -> service.updateRole(
                    myId, new UserRoleUpdateRequestDTO(RoleType.MANAGER), author))
                    .isInstanceOf(BusinessRuleException.class)
                    .hasMessage("You cannot change your own role");

            verifyNoInteractions(userRepository, roleRepository);
        }

        @Test
        @DisplayName("alterar o papel de outro usuário troca o papel e salva")
        void updateRole_anotherUser_changesRoleAndSaves() {
            UUID targetId = UUID.randomUUID();
            AuthenticatedUser author = authenticatedUser(UUID.randomUUID(), companyId, "ADMIN");

            Users target = Users.builder()
                    .id(targetId).name("Bruno").email("bruno@volta.com")
                    .company(company(companyId)).role(role("EMPLOYEE")).build();
            when(userRepository.findById(targetId)).thenReturn(Optional.of(target));
            when(roleRepository.findByType("MANAGER")).thenReturn(Optional.of(role("MANAGER")));

            UserResponseDTO response = service.updateRole(
                    targetId, new UserRoleUpdateRequestDTO(RoleType.MANAGER), author);

            assertThat(response.role()).isEqualTo("MANAGER");
            verify(userRepository).save(target);
        }

        @Test
        @DisplayName("usuário inexistente lança not found")
        void updateRole_missingUser_throwsNotFound() {
            UUID targetId = UUID.randomUUID();
            AuthenticatedUser author = authenticatedUser(UUID.randomUUID(), companyId, "ADMIN");
            when(userRepository.findById(targetId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.updateRole(
                    targetId, new UserRoleUpdateRequestDTO(RoleType.MANAGER), author))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("User not found");
        }
    }
}
