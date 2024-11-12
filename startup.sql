BEGIN;

INSERT INTO public.address (city, country, latitude, longitude, "number", street)
VALUES 
    ('Novi Sad', 'Serbia', 45.2600, 19.8335, '12', 'Bulevar Oslobođenja'),
    ('Novi Sad', 'Serbia', 45.2655, 19.8335, '8', 'Stražilovska'),
    ('Novi Sad', 'Serbia', 45.2700, 19.8335, '22', 'Železnička'),
    ('Novi Sad', 'Serbia', 45.2513, 19.8369, '5', 'Futoška'),
    ('Novi Sad', 'Serbia', 45.2565, 19.8483, '13', 'Zmaj Jovina'),
    ('Novi Sad', 'Serbia', 45.2610, 19.8226, '2', 'Cara Lazara'),
    ('Novi Sad', 'Serbia', 45.2644, 19.8313, '7', 'Narodnih Heroja'),
    ('Novi Sad', 'Serbia', 45.2854, 19.8300, '16', 'Jevrejska');

-- Password is '123' hashed as '$2a$10$Qtn2vCzJYM9QdiGIVnq59.LfB1YXZ0TVgNnOFNSQZCRQkc5QJoSoO'
INSERT INTO public.users (active, email, name, password, role, surname, username, verified, address_id)
VALUES  
    (TRUE, 'michael.smith@example.com', 'Michael', '$2a$10$Qtn2vCzJYM9QdiGIVnq59.LfB1YXZ0TVgNnOFNSQZCRQkc5QJoSoO', 1, 'Smith', 'michaelsmith', TRUE, 1),        -- Login: michaelsmith / 123
    (TRUE, 'emily.jones@example.com', 'Emily', '$2a$10$Qtn2vCzJYM9QdiGIVnq59.LfB1YXZ0TVgNnOFNSQZCRQkc5QJoSoO', 0, 'Jones', 'emilyjones', TRUE, 2),              -- Login: emilyjones / 123
    (TRUE, 'john.doe@example.com', 'John', '$2a$10$Qtn2vCzJYM9QdiGIVnq59.LfB1YXZ0TVgNnOFNSQZCRQkc5QJoSoO', 0, 'Doe', 'johndoe', TRUE, 3),                       -- Login: johndoe / 123
    (FALSE, 'jane.smith@example.com', 'Jane', '$2a$10$Qtn2vCzJYM9QdiGIVnq59.LfB1YXZ0TVgNnOFNSQZCRQkc5QJoSoO', 1, 'Smith', 'janesmith', TRUE, 4),                -- Login: janesmith / 123
    (TRUE, 'alice.johnson@example.com', 'Alice', '$2a$10$Qtn2vCzJYM9QdiGIVnq59.LfB1YXZ0TVgNnOFNSQZCRQkc5QJoSoO', 0, 'Johnson', 'alicejohnson', FALSE, 5),       -- Login: alicejohnson / 123
    (TRUE, 'bob.williams@example.com', 'Bob', '$2a$10$Qtn2vCzJYM9QdiGIVnq59.LfB1YXZ0TVgNnOFNSQZCRQkc5QJoSoO', 0, 'Williams', 'bobwilliams', TRUE, 6),           -- Login: bobwilliams / 123
    (FALSE, 'charlie.brown@example.com', 'Charlie', '$2a$10$Qtn2vCzJYM9QdiGIVnq59.LfB1YXZ0TVgNnOFNSQZCRQkc5QJoSoO', 1, 'Brown', 'charlieb', FALSE, 7),          -- Login: charlieb / 123
    (TRUE, 'david.davis@example.com', 'David', '$2a$10$Qtn2vCzJYM9QdiGIVnq59.LfB1YXZ0TVgNnOFNSQZCRQkc5QJoSoO', 0, 'Davis', 'daviddavis', TRUE, 8),              -- Login: daviddavis / 123
    (TRUE, 'eve.miller@example.com', 'Eve', '$2a$10$Qtn2vCzJYM9QdiGIVnq59.LfB1YXZ0TVgNnOFNSQZCRQkc5QJoSoO', 0, 'Miller', 'evemiller', FALSE, 1),                -- Login: evemiller / 123
    (TRUE, 'frank.garcia@example.com', 'Frank', '$2a$10$Qtn2vCzJYM9QdiGIVnq59.LfB1YXZ0TVgNnOFNSQZCRQkc5QJoSoO', 1, 'Garcia', 'frankgarcia', TRUE, 2),           -- Login: frankgarcia / 123
    (FALSE, 'grace.martinez@example.com', 'Grace', '$2a$10$Qtn2vCzJYM9QdiGIVnq59.LfB1YXZ0TVgNnOFNSQZCRQkc5QJoSoO', 0, 'Martinez', 'gracemartinez', TRUE, 3),    -- Login: gracemartinez / 123
    (TRUE, 'hannah.rodriguez@example.com', 'Hannah', '$2a$10$Qtn2vCzJYM9QdiGIVnq59.LfB1YXZ0TVgNnOFNSQZCRQkc5QJoSoO', 1, 'Rodriguez', 'hannahrod', FALSE, 4);    -- Login: hannahrod / 123

COMMIT;
