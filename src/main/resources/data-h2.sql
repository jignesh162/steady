/* Authorities aka. Roles */
INSERT INTO authorities (id, authority) VALUES (1, 'ROLE_USER');
INSERT INTO authorities (id, authority) VALUES (2, 'ROLE_ADMIN');
INSERT INTO authorities (id, authority) VALUES (3, 'ROLE_READONLY');

/* test: x */
INSERT INTO users (id, username, password, enabled, modified, modified_by) VALUES (1, 'test', '$2a$10$Lk58tJmfKnanOsfSQBqUv.y2.lPLIBApGNg1Mmy8HTetSJujY3m4u', true, '2019-12-01 11:53:52', 'h2admin');
INSERT INTO users_authorities (user_id, authority_id) VALUES (1, 1);

/* admin: y */
INSERT INTO users (id, username, password, enabled, modified, modified_by) VALUES (2, 'admin', '$2a$10$1T4Dh2D6QIDDrk4XuWkNHegpF1axcFQwSI.ZcBAFVRLuT6ZmkI73C', true, '2019-12-01 11:53:52', 'h2admin');
INSERT INTO users_authorities (user_id, authority_id) VALUES (2, 1);
INSERT INTO users_authorities (user_id, authority_id) VALUES (2, 2);

/* readonly: z */
INSERT INTO users (id, username, password, enabled, modified, modified_by) VALUES (3, 'readonly', '$2a$10$JRRGB1m3WqaFsF/QYHcPR.x3gT1tJnroygqe5ROKlhwp7J4RK3vgG', true, '2019-12-01 11:53:52', 'h2admin');
INSERT INTO users_authorities (user_id, authority_id) VALUES (3, 3);

/* Epic */
INSERT INTO epic (id, epic_id, epic_name, modified, modified_by) VALUES (1, 'SSABD-0000', 'test epic detail for ssabd-0000', '2019-12-01 11:53:52', 'h2admin');
INSERT INTO epic (id, epic_id, epic_name, modified, modified_by) VALUES (2, 'SSABD-9999', 'test epic detail for ssabd-9999', '2020-02-02 07:28:54', 'h2admin');
INSERT INTO epic (id, epic_id, epic_name, modified, modified_by) VALUES (3, 'SSABD-6313', 'test epic detail for ssabd-6313', '2019-09-02 11:34:54', 'h2admin');

/* Component */
INSERT INTO component (id, component_type, component, modified, modified_by) 
VALUES (1, 'COBOL', 'J13016U', '2019-12-12 12:12:12', 'h2admin');
INSERT INTO component (id, component_type, component, modified, modified_by) 
VALUES (2, 'COBOL', 'J13017U', '2020-12-18 12:13:13', 'h2admin');
INSERT INTO component (id, component_type, component, modified, modified_by) 
VALUES (3, 'COBOLSUB', 'J13017U', '2019-12-18 02:15:14', 'h2admin');
INSERT INTO component (id, component_type, component, modified, modified_by) 
VALUES (4, 'COBOL', 'J13020U', '2019-12-14 09:19:14', 'h2admin');
INSERT INTO component (id, component_type, component, modified, modified_by) 
VALUES (5, 'COBOL', 'J13021U', '2019-12-14 09:19:14', 'h2admin');
INSERT INTO component (id, component_type, component, modified, modified_by) 
VALUES (6, 'COBOL', 'J13022U', '2019-12-14 09:19:14', 'h2admin');
INSERT INTO component (id, component_type, component, modified, modified_by) 
VALUES (7, 'COBOL', 'J13023U', '2019-12-14 09:19:14', 'h2admin');

/* Developers */
INSERT INTO developer (id, name, active, modified, modified_by)
VALUES (1, 'Jignesh Parvadiya', true, '2019-12-13 12:13:13', 'h2admin');
INSERT INTO developer (id, name, active, modified, modified_by)
VALUES (2, 'Petteri Rissanen', true, '2019-12-13 12:13:13', 'h2admin');
INSERT INTO developer (id, name, active, modified, modified_by)
VALUES (3, 'Sanna Korhonen', true, '2020-02-13 12:13:13', 'h2admin');
INSERT INTO developer (id, name, active, modified, modified_by)
VALUES (4, 'Mika Lomu', false, '2019-12-13 12:13:13', 'h2admin');

/* Epic_Component */
INSERT INTO epic_component (id, component_id, epic_id, developer_id, ims, group_info, to_development, site, tested, to_production, status, description, go_live_desc, modified, modified_by) 
VALUES (1, 1, 1, 3, 'IMSB', 'Group info 1', '2019-11-05', 'RDT01', '2019-11-30', '2019-12-02', 'Maintenance', 'Component description 1', NULL, '2019-12-12 12:12:12', 'h2admin');
INSERT INTO epic_component (id, component_id, epic_id, developer_id, ims, group_info, to_development, site, tested, to_production, status, description, go_live_desc, modified, modified_by) 
VALUES (2, 2, 1, 2, 'IMSA', 'Group info 2', NULL, 'RDT01', NULL, '2019-12-03', 'Tested', NULL, NULL, '2019-12-13 12:13:13', 'h2admin');
INSERT INTO epic_component (id, component_id, epic_id, developer_id, ims, group_info, to_development, site, tested, to_production, status, description, go_live_desc, modified, modified_by) 
VALUES (3, 3, 1, 1, 'IMSA', 'Group info 2', NULL, 'RDT02', NULL, '2019-12-03', 'Maintenance', NULL, NULL, '2019-12-13 12:13:13', 'h2admin');
INSERT INTO epic_component (id, component_id, epic_id, developer_id, ims, group_info, to_development, site, tested, to_production, status, description, go_live_desc, modified, modified_by) 
VALUES (4, 4, 1, 3, 'IMSA', 'Group info 2', NULL, 'RDT01', NULL, '2019-12-03', 'Tested', NULL, NULL, '2019-12-13 12:13:13', 'h2admin');
INSERT INTO epic_component (id, component_id, epic_id, developer_id, ims, group_info, to_development, site, tested, to_production, status, description, go_live_desc, modified, modified_by) 
VALUES (5, 5, 1, 2, 'IMSA', 'Group info 2', NULL, 'RDT01', NULL, '2019-12-03', 'Maintenance', NULL, NULL, '2019-12-13 12:13:13', 'h2admin');
INSERT INTO epic_component (id, component_id, epic_id, developer_id, ims, group_info, to_development, site, tested, to_production, status, description, go_live_desc, modified, modified_by) 
VALUES (6, 6, 1, 2, 'IMSB', 'Group info 1', NULL, 'RDT01', NULL, '2019-12-03', 'Tested', NULL, NULL, '2019-12-13 12:13:13', 'h2admin');
INSERT INTO epic_component (id, component_id, epic_id, developer_id, ims, group_info, to_development, site, tested, to_production, status, description, go_live_desc, modified, modified_by) 
VALUES (7, 7, 1, 1, 'IMSB', 'Group info 1', NULL, 'RDT02', NULL, '2019-12-03', 'Maintenance', NULL, NULL, '2019-12-13 12:13:13', 'h2admin');
INSERT INTO epic_component (id, component_id, epic_id, developer_id, ims, group_info, to_development, site, tested, to_production, status, description, go_live_desc, modified, modified_by) 
VALUES (8, 4, 2, 3, 'IMSB', 'Group info 1', '2019-12-01', 'RDT01', NULL, NULL, 'Tested', 'Component description 2', NULL, '2019-12-14 12:14:14', 'h2admin');
INSERT INTO epic_component (id, component_id, epic_id, developer_id, ims, group_info, to_development, site, tested, to_production, status, description, go_live_desc, modified, modified_by) 
VALUES (9, 1, 2, 3, 'IMSA', 'Group info 2', '2019-12-01', 'RDT02', NULL, NULL, 'Maintenance', 'Component description 2', NULL, '2019-12-14 12:14:14', 'h2admin');
INSERT INTO epic_component (id, component_id, epic_id, developer_id, ims, group_info, to_development, site, tested, to_production, status, description, go_live_desc, modified, modified_by) 
VALUES (10, 2, 3, 3, 'IMSB', 'Group info 1', '2019-12-01', 'RDT01', NULL, NULL, 'Maintenance', 'Component description 2', 'Go live description...', '2019-12-14 12:14:14', 'h2admin');
