package com.volta.api.controller.docs;

import com.volta.api.dto.request.UserRequestDTO;
import com.volta.api.dto.request.update.UserRoleUpdateRequestDTO;
import com.volta.api.dto.request.update.UserUpdateRequestDTO;
import com.volta.api.dto.response.UserResponseDTO;
import com.volta.api.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Usuários", description = "Cadastro de usuários, perfil do usuário autenticado e gestão de perfis de acesso")
public interface UserControllerDocs {

    @Operation(summary = "Cadastra um usuário", description = "**Acesso:** ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado")
    })
    ResponseEntity<UserResponseDTO> create(UserRequestDTO dto);

    @Operation(summary = "Lista todos os usuários", description = "**Acesso:** ADMIN.")
    @ApiResponse(responseCode = "200", description = "Lista de usuários")
    ResponseEntity<List<UserResponseDTO>> show();

    @Operation(summary = "Busca um usuário pelo ID", description = "**Acesso:** ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    ResponseEntity<UserResponseDTO> showById(@Parameter(description = "ID do usuário") UUID id);

    @Operation(
            summary = "Consulta o próprio perfil",
            description = "Retorna os dados do usuário autenticado. **Acesso:** qualquer usuário autenticado."
    )
    @ApiResponse(responseCode = "200", description = "Dados do usuário autenticado")
    ResponseEntity<UserResponseDTO> showMe(@Parameter(hidden = true) AuthenticatedUser author);

    @Operation(
            summary = "Atualiza o próprio perfil",
            description = "Altera o nome e o cargo do usuário autenticado. **Acesso:** qualquer usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    ResponseEntity<UserResponseDTO> update(UserUpdateRequestDTO dto, @Parameter(hidden = true) AuthenticatedUser author);

    @Operation(
            summary = "Altera o perfil de acesso de um usuário",
            description = "Troca a role do usuário. O administrador não pode alterar a própria role. **Acesso:** ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil de acesso atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "422", description = "Tentativa de alterar a própria role")
    })
    ResponseEntity<UserResponseDTO> updateRole(
            @Parameter(description = "ID do usuário") UUID id,
            UserRoleUpdateRequestDTO dto,
            @Parameter(hidden = true) AuthenticatedUser author
    );
}
