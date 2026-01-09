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
