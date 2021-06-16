CREATE TABLE IF NOT EXISTS privileges (
    id SERIAL,
    name character varying(255),
    created_by character varying(255),
    created_at date,
    updated_by character varying(255),
    updated_at date,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS roles (
    id SERIAL,
    name character varying(255),
    created_by character varying(255),
    created_at date,
    updated_by character varying(255),
    updated_at date,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS roles_privileges (
    role_id integer NOT NULL,
    privilege_id integer NOT NULL,
    PRIMARY KEY (role_id, privilege_id),
    FOREIGN KEY (privilege_id) REFERENCES privileges(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL,
    login_id character varying(255),
    password character varying(255),
    locked boolean,
    enabled boolean,
    expire boolean,
    expire_at date,
    created_by character varying(255),
    created_at date,
    updated_by character varying(255),
    updated_at date,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users_roles (
     user_id integer NOT NULL,
     role_id integer NOT NULL,
     PRIMARY KEY (user_id, role_id),
     FOREIGN KEY (user_id) REFERENCES users(id),
     FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS audit_logs (
    id INTEGER NOT NULL,
    action VARCHAR(255),
    created_by VARCHAR(255),
    entity VARCHAR(255),
    timestamp timestamp,
    value text,
    PRIMARY KEY (ID)
);

INSERT INTO privileges (id, name, created_at) VALUES (1, 'EMERALD_MANAGEMENT', NOW());

INSERT INTO roles (id, name, created_at) VALUES (1, 'ROLE_ADMIN', NOW());
INSERT INTO roles (id, name, created_at) VALUES (2, 'ROLE_USER', NOW());

INSERT INTO roles_privileges (role_id, privilege_id) VALUES (1, 1);

-- password: Admin_00
INSERT INTO users (id, login_id, password, enabled, locked, expire)
    VALUES (100, 'admin@brsrker.com', '$2a$10$4Y/Mx5Djg2Oo63JsMY1ma.xMPgqGMPSwTDIYJHZlKdrk5WCKeQ7Ji', true, false, false);

INSERT INTO users_roles (user_id, role_id) VALUES (100, 1);