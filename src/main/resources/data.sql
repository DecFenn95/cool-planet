CREATE TABLE task_type_overview(
   id SERIAL PRIMARY KEY,
   task_identifier VARCHAR(30) NOT NULL UNIQUE,
   current_average DOUBLE PRECISION NOT NULL,
   updated_at TIMESTAMP NOT NULL
);

INSERT INTO task_type_overview (task_identifier, current_average, updated_at) VALUES ("A", 0.0, "2023-01-01 12:00:00.0000");
INSERT INTO task_type_overview (task_identifier, current_average, updated_at) VALUES ("B", 0.0, "2023-01-01 12:00:00.0000");
INSERT INTO task_type_overview (task_identifier, current_average, updated_at) VALUES ("C", 0.0, "2023-01-01 12:00:00.0000");
INSERT INTO task_type_overview (task_identifier, current_average, updated_at) VALUES ("D", 0.0, "2023-01-01 12:00:00.0000");
INSERT INTO task_type_overview (task_identifier, current_average, updated_at) VALUES ("E", 0.0, "2023-01-01 12:00:00.0000");

CREATE TABLE task_type_event (
     id SERIAL PRIMARY KEY,
     task_identifier VARCHAR(30) NOT NULL,
     duration DOUBLE PRECISION NOT NULL,
     created_at TIMESTAMP NOT NULL
);

INSERT INTO tast_type_event (task_identifier, duration, created_at) VALUES ('A', 5.0,'2023-02-05 11:50:00.0000');
INSERT INTO tast_type_event (task_identifier, duration, created_at) VALUES ('A', 10.0,'2023-02-08 18:47:00.0000');
INSERT INTO tast_type_event (task_identifier, duration, created_at) VALUES ('A', 2.0,'2023-02-09 09:24:36.0000');
INSERT INTO tast_type_event (task_identifier, duration, created_at) VALUES ('A', 3.0,'2023-02-09 13:17:38.1001');
INSERT INTO tast_type_event (task_identifier, duration, created_at) VALUES ('B', 2.0,'2023-02-05 11:50:00.0000');
INSERT INTO tast_type_event (task_identifier, duration, created_at) VALUES ('B', 2.0,'2023-02-08 18:47:00.0000');
INSERT INTO tast_type_event (task_identifier, duration, created_at) VALUES ('B', 2.0,'2023-02-09 09:24:36.0000');
INSERT INTO tast_type_event (task_identifier, duration, created_at) VALUES ('B', 14.0,'2023-02-09 13:17:38.1001');
INSERT INTO tast_type_event (task_identifier, duration, created_at) VALUES ('B', 12.0,'2023-02-10 14:19:28.1000');
INSERT INTO tast_type_event (task_identifier, duration, created_at) VALUES ('C', 6.0,'2023-01-15 15:50:00.0000');
INSERT INTO tast_type_event (task_identifier, duration, created_at) VALUES ('D', 11.0,'2023-01-23 08:33:22.0000');
INSERT INTO tast_type_event (task_identifier, duration, created_at) VALUES ('D', 7.0,'2023-02-08 17:47:00.0000');
INSERT INTO tast_type_event (task_identifier, duration, created_at) VALUES ('E', 2.8,'2023-02-09 07:14:26.0000');
INSERT INTO tast_type_event (task_identifier, duration, created_at) VALUES ('E', 1.4,'2023-01-09 19:19:18.1001');