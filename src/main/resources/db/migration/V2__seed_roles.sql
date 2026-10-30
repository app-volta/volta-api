-- Data Load para roles
INSERT INTO role (type) VALUES
                            ('ADMIN'),
                            ('EMPLOYEE'),
                            ('MANAGER')
    ON CONFLICT (type) DO NOTHING;