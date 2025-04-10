-- Table: admin
INSERT INTO `admin` (`id`, `email`, `password`) VALUES (1, 'admin@example.com', 'password1');
INSERT INTO `admin` (`id`, `email`, `password`) VALUES (2, 'admin2@example.com', 'password2');


-- Table: member
INSERT INTO `member` (`id`, `address`, `birthdate`, `email`, `firstname`, `lastname`, `password`, `phone_number`, `registration_date`,`is_subscription_validated`,`is_payement_validated`) VALUES (1, '123 Street Name', '1990-01-01', 'member@example.com', 'John', 'Doe', 'password', '123-456-7890', '2024-01-01',false,false);


-- Table: reservation
INSERT INTO `reservation` (`id`, `ordered_date`, `validate_date`,`is_validate`, `member_id`) VALUES (1, '2024-04-22', '2024-04-22',false, 1, 1);
INSERT INTO `reservation` (`id`, `ordered_date`, `validate_date`,`is_validate`, `member_id`) VALUES (2, '2024-04-23', '2024-04-22',false, 1, 1);

-- Table: role
INSERT INTO `role` (`id`, `name`) VALUES (1, 'Admin');
INSERT INTO `role` (`id`, `name`) VALUES (2, 'Member');

-- Table: session_type
INSERT INTO `session_type` (`id`, `name`) VALUES (1, 'Boxing');

-- Table: session
INSERT INTO `session` (`id`, `coach_name`, `date`, `description`, `duration_in_hours`, `hour`, `max_people`, `name`, `admin_id`, `session_type_id`) VALUES (1, 'Coach 1', '2024-04-25', 'Session 1 description', 1, 10, 10, 'Session 1', 1, 1);
INSERT INTO `session` (`id`, `coach_name`, `date`, `description`, `duration_in_hours`, `hour`, `max_people`, `name`, `admin_id`, `session_type_id`) VALUES (2, 'Coach 2', '2024-04-26', 'Session 2 description', 1, 10, 15, 'Session 2', 2, 1);

-- Table: member_roles
INSERT INTO `member_roles` (`member_id`, `roles_id`) VALUES (1, 2);

-- Table: admin_roles
INSERT INTO `admin_roles` (`admin_id`, `roles_id`) VALUES (1, 1);
INSERT INTO `admin_roles` (`admin_id`, `roles_id`) VALUES (2, 2);

-- Table: reservation_session
INSERT INTO `reservation_sessions` (`reservation_id`, `sessions_id`) VALUES (1, 1);

