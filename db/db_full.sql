

DROP DATABASE IF EXISTS dodo_pizza;
CREATE DATABASE dodo_pizza
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE dodo_pizza;

CREATE TABLE roles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL UNIQUE,
  display_name_sk VARCHAR(50) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(120) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  first_name VARCHAR(60) NOT NULL,
  last_name VARCHAR(60) NOT NULL,
  phone VARCHAR(30),
  delivery_address VARCHAR(255),
  profile_image_url VARCHAR(500),
  enabled BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE user_roles (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk_user_roles_user
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE CASCADE,
  CONSTRAINT fk_user_roles_role
    FOREIGN KEY (role_id) REFERENCES roles(id)
    ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE pizzas (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name_sk VARCHAR(120) NOT NULL,
  description_sk VARCHAR(800),
  slug VARCHAR(140) NOT NULL UNIQUE,
  image_url VARCHAR(500),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE pizza_sizes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  pizza_id BIGINT NOT NULL,
  size_code ENUM('S','M','L') NOT NULL,
  diameter_cm INT NOT NULL,
  price_eur DECIMAL(10,2) NOT NULL,
  available BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT uq_pizza_size UNIQUE (pizza_id, size_code),
  CONSTRAINT fk_pizza_sizes_pizza
    FOREIGN KEY (pizza_id) REFERENCES pizzas(id)
    ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE ingredients (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name_sk VARCHAR(120) NOT NULL UNIQUE,
  allergen_info_sk VARCHAR(120),
  vegan BOOLEAN NOT NULL DEFAULT FALSE,
  spicy BOOLEAN NOT NULL DEFAULT FALSE,
  extra_price_eur DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE tags (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name_sk VARCHAR(80) NOT NULL UNIQUE,
  slug VARCHAR(100) NOT NULL UNIQUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE pizza_ingredients (
  pizza_id BIGINT NOT NULL,
  ingredient_id BIGINT NOT NULL,
  PRIMARY KEY (pizza_id, ingredient_id),
  CONSTRAINT fk_pizza_ing_pizza
    FOREIGN KEY (pizza_id) REFERENCES pizzas(id)
    ON DELETE CASCADE,
  CONSTRAINT fk_pizza_ing_ing
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
    ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE pizza_tags (
  pizza_id BIGINT NOT NULL,
  tag_id BIGINT NOT NULL,
  PRIMARY KEY (pizza_id, tag_id),
  CONSTRAINT fk_pizza_tag_pizza
    FOREIGN KEY (pizza_id) REFERENCES pizzas(id)
    ON DELETE CASCADE,
  CONSTRAINT fk_pizza_tag_tag
    FOREIGN KEY (tag_id) REFERENCES tags(id)
    ON DELETE RESTRICT
) ENGINE=InnoDB;


CREATE TABLE orders (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  order_number VARCHAR(30) NOT NULL UNIQUE,
  status ENUM('PENDING','PREPARING','READY','DELIVERING','DELIVERED','CANCELLED') NOT NULL DEFAULT 'PENDING',
  delivery_address VARCHAR(255) NOT NULL,
  customer_note VARCHAR(800),
  total_price_eur DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_orders_user
    FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE order_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  size_id BIGINT,
  pizza_name_snapshot VARCHAR(120) NOT NULL,
  size_snapshot ENUM('S','M','L') NOT NULL,
  diameter_snapshot_cm INT NOT NULL,
  unit_price_snapshot_eur DECIMAL(10,2) NOT NULL,
  quantity INT NOT NULL,
  line_total_eur DECIMAL(10,2) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_items_order
    FOREIGN KEY (order_id) REFERENCES orders(id)
    ON DELETE CASCADE,
  CONSTRAINT fk_items_size
    FOREIGN KEY (size_id) REFERENCES pizza_sizes(id)
    ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE order_item_extras (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_item_id BIGINT NOT NULL,
  ingredient_id BIGINT,
  ingredient_name_snapshot VARCHAR(120) NOT NULL,
  extra_price_snapshot_eur DECIMAL(10,2) NOT NULL,
  quantity INT NOT NULL DEFAULT 1,
  line_total_eur DECIMAL(10,2) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_extras_item
    FOREIGN KEY (order_item_id) REFERENCES order_items(id)
    ON DELETE CASCADE,
  CONSTRAINT fk_extras_ingredient
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
    ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE INDEX idx_pizzas_active ON pizzas(active);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_user ON orders(user_id);
CREATE INDEX idx_pizza_sizes_pizza ON pizza_sizes(pizza_id);
CREATE INDEX idx_extras_item ON order_item_extras(order_item_id);




-- Demo dáta
USE dodo_pizza;

DELETE FROM order_item_extras;
DELETE FROM order_items;
DELETE FROM orders;
DELETE FROM pizza_tags;
DELETE FROM pizza_ingredients;
DELETE FROM tags;
DELETE FROM ingredients;
DELETE FROM pizza_sizes;
DELETE FROM pizzas;
DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM roles;

-- Roly
INSERT INTO roles (name, display_name_sk) VALUES
('ROLE_ANONYMOUS', 'Anonymný používateľ'),
('ROLE_CUSTOMER', 'Zákazník'),
('ROLE_KITCHEN', 'Kuchár / manažér'),
('ROLE_COURIER', 'Kuriér'),
('ROLE_ADMIN', 'Admin / majiteľ');

-- Používatelia (heslá sú dočasné hodnoty, v aplikácii sa budú ukladať ako hash)
INSERT INTO users (email, password_hash, first_name, last_name, phone, delivery_address, profile_image_url, enabled) VALUES
('zakaznik@test.sk', 'temp', 'Peter', 'Novák', '+421900111222', 'Nitra, Štúrova 10', '/img/profiles/user1.jpg', TRUE),
('kuchar@test.sk', 'temp', 'Marek', 'Kováč', '+421900333444', 'Nitra, Mostná 5', '/img/profiles/user2.jpg', TRUE),
('kurier@test.sk', 'temp', 'Ján', 'Horváth', '+421900555666', 'Nitra, Chrenová 2', '/img/profiles/user3.jpg', TRUE),
('admin@test.sk', 'temp', 'Eva', 'Šimeková', '+421900777888', 'Nitra, Centrum 1', '/img/profiles/user4.jpg', TRUE);

-- Priradenie rolí používateľom
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_CUSTOMER'
WHERE u.email = 'zakaznik@test.sk';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_KITCHEN'
WHERE u.email = 'kuchar@test.sk';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_COURIER'
WHERE u.email = 'kurier@test.sk';

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_ADMIN'
WHERE u.email = 'admin@test.sk';

-- Tagy
INSERT INTO tags (name_sk, slug) VALUES
('Vegetariánska', 'vegetarianska'),
('Pikantná', 'pikantna'),
('Novinka', 'novinka'),
('Bestseller', 'bestseller'),
('Bezlepková', 'bezlepkova');

-- Ingrediencie (15 ks)
INSERT INTO ingredients (name_sk, allergen_info_sk, vegan, spicy, extra_price_eur) VALUES
('Mozzarella', '7', FALSE, FALSE, 1.20),
('Parmezán', '7', FALSE, FALSE, 1.50),
('Šunka', '1', FALSE, FALSE, 1.50),
('Saláma', '1', FALSE, FALSE, 1.50),
('Slanina', '1', FALSE, FALSE, 1.70),
('Kuracie mäso', NULL, FALSE, FALSE, 1.80),
('Tuniak', '4', FALSE, FALSE, 1.80),
('Olivy', NULL, TRUE, FALSE, 1.00),
('Kukurica', NULL, TRUE, FALSE, 0.90),
('Šampiňóny', NULL, TRUE, FALSE, 1.00),
('Cibuľa', NULL, TRUE, FALSE, 0.70),
('Paprika', NULL, TRUE, FALSE, 0.80),
('Jalapeño', NULL, TRUE, TRUE, 0.90),
('Ananás', NULL, TRUE, FALSE, 1.00),
('Rukola', NULL, TRUE, FALSE, 1.00);


INSERT INTO pizzas (id, name_sk, description_sk, slug, image_url, active, created_at, updated_at) VALUES
(1,  'Margherita','Paradajkový základ, mozzarella, bazalka.','margherita','/uploads/pizzas/margherita-classica.webp',1, NOW(), NOW()),
(2,  'Šunková','Paradajkový základ, mozzarella, šunka.','sunkova','/uploads/pizzas/capricciosa.webp',1, NOW(), NOW()),
(3,  'Salámová','Paradajkový základ, mozzarella, saláma.','salamova','/uploads/pizzas/delicatezza-rustica.webp',1, NOW(), NOW()),
(4,  'Hawai','Paradajkový základ, mozzarella, šunka, ananás.','hawai','/uploads/pizzas/hawaii-classic.webp',1, NOW(), NOW()),
(5,  'Quattro Formaggi', 'Mozzarella, parmezán a ďalšie syry.','quattro-formaggi', '/uploads/pizzas/la-crema-bianca.webp',1, NOW(), NOW()),
(6,  'Funghi','Paradajkový základ, mozzarella, šampiňóny.','funghi','/uploads/pizzas/funghi.webp',1, NOW(), NOW()),
(7,  'Vegetariánska','Zelenina, mozzarella, olivy.','vegetarianska','/uploads/pizzas/gluten-free-primavera.webp',1, NOW(), NOW()),
(8,  'Diavola','Pikantná saláma, jalapeño, mozzarella.','diavola','/uploads/pizzas/diavola-piccante.webp',1, NOW(), NOW()),
(9,  'Tuniaková','Paradajkový základ, mozzarella, tuniak, cibuľa.','tuniakova','/uploads/pizzas/carbonara-pizza.webp',1, NOW(), NOW()),
(10, 'Kuracia BBQ','BBQ základ, mozzarella, kuracie mäso, cibuľa.','kuracia-bbq','/uploads/pizzas/funghi-al-panna.webp',1, NOW(), NOW());

-- Veľkosti pre každú pizzu (S/M/L)
INSERT INTO pizza_sizes (pizza_id, size_code, diameter_cm, price_eur, available)
SELECT p.id, 'S', 25,
  CASE p.slug
    WHEN 'margherita' THEN 6.50
    WHEN 'sunkova' THEN 7.20
    WHEN 'salamova' THEN 7.20
    WHEN 'hawai' THEN 7.80
    WHEN 'quattro-formaggi' THEN 8.20
    WHEN 'funghi' THEN 7.10
    WHEN 'vegetarianska' THEN 7.30
    WHEN 'diavola' THEN 7.90
    WHEN 'tuniakova' THEN 8.10
    WHEN 'kuracia-bbq' THEN 8.30
    ELSE 7.00
  END,
  TRUE
FROM pizzas p;

INSERT INTO pizza_sizes (pizza_id, size_code, diameter_cm, price_eur, available)
SELECT p.id, 'M', 30,
  CASE p.slug
    WHEN 'margherita' THEN 8.50
    WHEN 'sunkova' THEN 9.20
    WHEN 'salamova' THEN 9.20
    WHEN 'hawai' THEN 9.90
    WHEN 'quattro-formaggi' THEN 10.40
    WHEN 'funghi' THEN 9.10
    WHEN 'vegetarianska' THEN 9.30
    WHEN 'diavola' THEN 10.10
    WHEN 'tuniakova' THEN 10.30
    WHEN 'kuracia-bbq' THEN 10.60
    ELSE 9.00
  END,
  TRUE
FROM pizzas p;

INSERT INTO pizza_sizes (pizza_id, size_code, diameter_cm, price_eur, available)
SELECT p.id, 'L', 35,
  CASE p.slug
    WHEN 'margherita' THEN 10.50
    WHEN 'sunkova' THEN 11.40
    WHEN 'salamova' THEN 11.40
    WHEN 'hawai' THEN 12.20
    WHEN 'quattro-formaggi' THEN 12.80
    WHEN 'funghi' THEN 11.20
    WHEN 'vegetarianska' THEN 11.40
    WHEN 'diavola' THEN 12.40
    WHEN 'tuniakova' THEN 12.60
    WHEN 'kuracia-bbq' THEN 13.00
    ELSE 11.00
  END,
  TRUE
FROM pizzas p;

-- Prepojenie pizze - tagy
INSERT INTO pizza_tags (pizza_id, tag_id)
SELECT p.id, t.id
FROM pizzas p
JOIN tags t ON t.slug = 'bestseller'
WHERE p.slug IN ('margherita', 'sunkova', 'diavola');

INSERT INTO pizza_tags (pizza_id, tag_id)
SELECT p.id, t.id
FROM pizzas p
JOIN tags t ON t.slug = 'vegetarianska'
WHERE p.slug IN ('margherita', 'vegetarianska', 'funghi');

INSERT INTO pizza_tags (pizza_id, tag_id)
SELECT p.id, t.id
FROM pizzas p
JOIN tags t ON t.slug = 'pikantna'
WHERE p.slug IN ('diavola');

INSERT INTO pizza_tags (pizza_id, tag_id)
SELECT p.id, t.id
FROM pizzas p
JOIN tags t ON t.slug = 'novinka'
WHERE p.slug IN ('kuracia-bbq');

-- Základné recepty (pizza_ingredients)
INSERT INTO pizza_ingredients (pizza_id, ingredient_id)
SELECT p.id, i.id
FROM pizzas p
JOIN ingredients i ON i.name_sk IN ('Mozzarella')
WHERE p.slug IN ('margherita','sunkova','salamova','hawai','funghi','vegetarianska','diavola','tuniakova','kuracia-bbq','quattro-formaggi');

INSERT INTO pizza_ingredients (pizza_id, ingredient_id)
SELECT p.id, i.id
FROM pizzas p
JOIN ingredients i ON i.name_sk = 'Šunka'
WHERE p.slug IN ('sunkova','hawai');

INSERT INTO pizza_ingredients (pizza_id, ingredient_id)
SELECT p.id, i.id
FROM pizzas p
JOIN ingredients i ON i.name_sk = 'Saláma'
WHERE p.slug IN ('salamova','diavola');

INSERT INTO pizza_ingredients (pizza_id, ingredient_id)
SELECT p.id, i.id
FROM pizzas p
JOIN ingredients i ON i.name_sk IN ('Šampiňóny')
WHERE p.slug IN ('funghi');

INSERT INTO pizza_ingredients (pizza_id, ingredient_id)
SELECT p.id, i.id
FROM pizzas p
JOIN ingredients i ON i.name_sk IN ('Olivy','Paprika','Cibuľa','Kukurica')
WHERE p.slug IN ('vegetarianska');

INSERT INTO pizza_ingredients (pizza_id, ingredient_id)
SELECT p.id, i.id
FROM pizzas p
JOIN ingredients i ON i.name_sk IN ('Tuniak','Cibuľa')
WHERE p.slug IN ('tuniakova');

INSERT INTO pizza_ingredients (pizza_id, ingredient_id)
SELECT p.id, i.id
FROM pizzas p
JOIN ingredients i ON i.name_sk IN ('Kuracie mäso','Cibuľa')
WHERE p.slug IN ('kuracia-bbq');

INSERT INTO pizza_ingredients (pizza_id, ingredient_id)
SELECT p.id, i.id
FROM pizzas p
JOIN ingredients i ON i.name_sk IN ('Parmezán')
WHERE p.slug IN ('quattro-formaggi');

-- Objednávky (5 ks v rôznych stavoch)
INSERT INTO orders (user_id, order_number, status, delivery_address, customer_note, total_price_eur)
SELECT u.id, 'ORD-2026-0001', 'pending', 'Nitra, Štúrova 10', 'Prosím bez cibule.', 0.00
FROM users u WHERE u.email = 'zakaznik@test.sk';

INSERT INTO orders (user_id, order_number, status, delivery_address, customer_note, total_price_eur)
SELECT u.id, 'ORD-2026-0002', 'preparing', 'Nitra, Štúrova 10', NULL, 0.00
FROM users u WHERE u.email = 'zakaznik@test.sk';

INSERT INTO orders (user_id, order_number, status, delivery_address, customer_note, total_price_eur)
SELECT u.id, 'ORD-2026-0003', 'ready', 'Nitra, Štúrova 10', 'Zazvoňte prosím.', 0.00
FROM users u WHERE u.email = 'zakaznik@test.sk';

INSERT INTO orders (user_id, order_number, status, delivery_address, customer_note, total_price_eur)
SELECT u.id, 'ORD-2026-0004', 'delivering', 'Nitra, Štúrova 10', NULL, 0.00
FROM users u WHERE u.email = 'zakaznik@test.sk';

INSERT INTO orders (user_id, order_number, status, delivery_address, customer_note, total_price_eur)
SELECT u.id, 'ORD-2026-0005', 'delivered', 'Nitra, Štúrova 10', NULL, 0.00
FROM users u WHERE u.email = 'zakaznik@test.sk';

-- Položky objednávok (2 položky na niektoré objednávky)
-- ORD-2026-0001: Margherita M x1 + extra Parmezán x1
INSERT INTO order_items (order_id, size_id, pizza_name_snapshot, size_snapshot, diameter_snapshot_cm, unit_price_snapshot_eur, quantity, line_total_eur)
SELECT o.id, ps.id, 'Margherita', 'M', 30, ps.price_eur, 1, ps.price_eur
FROM orders o
JOIN pizzas p ON p.slug = 'margherita'
JOIN pizza_sizes ps ON ps.pizza_id = p.id AND ps.size_code = 'M'
WHERE o.order_number = 'ORD-2026-0001';

INSERT INTO order_item_extras (order_item_id, ingredient_id, ingredient_name_snapshot, extra_price_snapshot_eur, quantity, line_total_eur)
SELECT oi.id, i.id, i.name_sk, i.extra_price_eur, 1, i.extra_price_eur
FROM order_items oi
JOIN orders o ON o.id = oi.order_id AND o.order_number = 'ORD-2026-0001'
JOIN ingredients i ON i.name_sk = 'Parmezán'
LIMIT 1;

-- ORD-2026-0002: Diavola L x1 + extra Jalapeño x2
INSERT INTO order_items (order_id, size_id, pizza_name_snapshot, size_snapshot, diameter_snapshot_cm, unit_price_snapshot_eur, quantity, line_total_eur)
SELECT o.id, ps.id, 'Diavola', 'L', 35, ps.price_eur, 1, ps.price_eur
FROM orders o
JOIN pizzas p ON p.slug = 'diavola'
JOIN pizza_sizes ps ON ps.pizza_id = p.id AND ps.size_code = 'L'
WHERE o.order_number = 'ORD-2026-0002';

INSERT INTO order_item_extras (order_item_id, ingredient_id, ingredient_name_snapshot, extra_price_snapshot_eur, quantity, line_total_eur)
SELECT oi.id, i.id, i.name_sk, i.extra_price_eur, 2, (i.extra_price_eur * 2)
FROM order_items oi
JOIN orders o ON o.id = oi.order_id AND o.order_number = 'ORD-2026-0002'
JOIN ingredients i ON i.name_sk = 'Jalapeño'
LIMIT 1;

-- ORD-2026-0003: Hawai S x2 (bez extra)
INSERT INTO order_items (order_id, size_id, pizza_name_snapshot, size_snapshot, diameter_snapshot_cm, unit_price_snapshot_eur, quantity, line_total_eur)
SELECT o.id, ps.id, 'Hawai', 'S', 25, ps.price_eur, 2, (ps.price_eur * 2)
FROM orders o
JOIN pizzas p ON p.slug = 'hawai'
JOIN pizza_sizes ps ON ps.pizza_id = p.id AND ps.size_code = 'S'
WHERE o.order_number = 'ORD-2026-0003';

-- ORD-2026-0004: Tuniaková M x1 + extra Olivy x1
INSERT INTO order_items (order_id, size_id, pizza_name_snapshot, size_snapshot, diameter_snapshot_cm, unit_price_snapshot_eur, quantity, line_total_eur)
SELECT o.id, ps.id, 'Tuniaková', 'M', 30, ps.price_eur, 1, ps.price_eur
FROM orders o
JOIN pizzas p ON p.slug = 'tuniakova'
JOIN pizza_sizes ps ON ps.pizza_id = p.id AND ps.size_code = 'M'
WHERE o.order_number = 'ORD-2026-0004';

INSERT INTO order_item_extras (order_item_id, ingredient_id, ingredient_name_snapshot, extra_price_snapshot_eur, quantity, line_total_eur)
SELECT oi.id, i.id, i.name_sk, i.extra_price_eur, 1, i.extra_price_eur
FROM order_items oi
JOIN orders o ON o.id = oi.order_id AND o.order_number = 'ORD-2026-0004'
JOIN ingredients i ON i.name_sk = 'Olivy'
LIMIT 1;

-- ORD-2026-0005: Kuracia BBQ M x1 + extra Slanina x1
INSERT INTO order_items (order_id, size_id, pizza_name_snapshot, size_snapshot, diameter_snapshot_cm, unit_price_snapshot_eur, quantity, line_total_eur)
SELECT o.id, ps.id, 'Kuracia BBQ', 'M', 30, ps.price_eur, 1, ps.price_eur
FROM orders o
JOIN pizzas p ON p.slug = 'kuracia-bbq'
JOIN pizza_sizes ps ON ps.pizza_id = p.id AND ps.size_code = 'M'
WHERE o.order_number = 'ORD-2026-0005';

INSERT INTO order_item_extras (order_item_id, ingredient_id, ingredient_name_snapshot, extra_price_snapshot_eur, quantity, line_total_eur)
SELECT oi.id, i.id, i.name_sk, i.extra_price_eur, 1, i.extra_price_eur
FROM order_items oi
JOIN orders o ON o.id = oi.order_id AND o.order_number = 'ORD-2026-0005'
JOIN ingredients i ON i.name_sk = 'Slanina'
LIMIT 1;

-- Prepočet total_price_eur podľa položiek a extra prísad
UPDATE orders o
SET o.total_price_eur =
  (
    SELECT IFNULL(SUM(oi.line_total_eur), 0)
    FROM order_items oi
    WHERE oi.order_id = o.id
  )
  +
  (
    SELECT IFNULL(SUM(oe.line_total_eur), 0)
    FROM order_item_extras oe
    JOIN order_items oi2 ON oi2.id = oe.order_item_id
    WHERE oi2.order_id = o.id
  );
