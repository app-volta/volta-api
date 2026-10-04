-- Data Load para roles
INSERT INTO role (type) VALUES
                            ('ADMIN'),
                            ('EMPLOYEE'),
                            ('OPERATOR'),
                            ('MANAGER')
    ON CONFLICT (type) DO NOTHING;