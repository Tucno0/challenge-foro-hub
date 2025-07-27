-- Insert default profiles
INSERT INTO perfiles (nombre)
VALUES ('ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO perfiles (nombre)
VALUES ('USER') ON CONFLICT DO NOTHING;

-- Insert a default admin user (password is 'admin123' encoded with BCrypt)
-- Note: In production, you should change this password immediately
INSERT INTO usuarios (nombre, correo_electronico, contrasena)
VALUES (
    'Administrador',
    'admin@forohub.com',
    '$2a$12$nK3nv7KxSj.Nqpcd8KM9sOEFo.aPK6RG9V0ZAfCYnprEwDOtmMKBa'
  ) ON CONFLICT (correo_electronico) DO NOTHING;

-- Assign ADMIN profile to the admin user
INSERT INTO usuarios_perfiles (usuario_id, perfil_id)
SELECT u.id,
  p.id
FROM usuarios u,
  perfiles p
WHERE u.correo_electronico = 'admin@forohub.com'
  AND p.nombre = 'ADMIN' ON CONFLICT DO NOTHING;