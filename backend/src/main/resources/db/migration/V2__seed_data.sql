INSERT INTO users (id, full_name, email, password_hash, role, enabled) VALUES
('11111111-1111-1111-1111-111111111111', 'System Admin', 'admin@smartparkpro.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', true),
('22222222-2222-2222-2222-222222222222', 'Operations Staff', 'staff@smartparkpro.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'STAFF', true),
('33333333-3333-3333-3333-333333333333', 'Customer One', 'customer@smartparkpro.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'CUSTOMER', true);

INSERT INTO parking_lots (id, name, address, capacity, hourly_rate, active) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Central Business Garage', '100 Main Street', 120, 5.50, true),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Airport Express Parking', '45 Terminal Road', 240, 7.25, true);

INSERT INTO parking_slots (id, parking_lot_id, slot_code, supported_vehicle_type, status, ev_charger) VALUES
('aaaaaaaa-1111-1111-1111-aaaaaaaaaaaa', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'A-01', 'CAR', 'AVAILABLE', false),
('aaaaaaaa-2222-2222-2222-aaaaaaaaaaaa', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'A-02', 'EV', 'AVAILABLE', true),
('aaaaaaaa-3333-3333-3333-aaaaaaaaaaaa', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'A-03', 'CAR', 'OCCUPIED', false),
('bbbbbbbb-1111-1111-1111-bbbbbbbbbbbb', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'B-01', 'VAN', 'AVAILABLE', false);

INSERT INTO vehicles (id, owner_id, license_plate, vehicle_type, make, model, color) VALUES
('44444444-4444-4444-4444-444444444444', '33333333-3333-3333-3333-333333333333', 'SPP-1001', 'CAR', 'Toyota', 'Corolla', 'Silver'),
('55555555-5555-5555-5555-555555555555', '33333333-3333-3333-3333-333333333333', 'SPP-EV01', 'EV', 'Tesla', 'Model 3', 'White');

INSERT INTO bookings (id, user_id, vehicle_id, parking_slot_id, start_time, end_time, status, estimated_cost) VALUES
('66666666-6666-6666-6666-666666666666', '33333333-3333-3333-3333-333333333333', '44444444-4444-4444-4444-444444444444', 'aaaaaaaa-3333-3333-3333-aaaaaaaaaaaa', NOW() - INTERVAL '2 hours', NOW() + INTERVAL '1 hour', 'CHECKED_IN', 16.50);

INSERT INTO payments (id, booking_id, amount, method, status, transaction_reference, paid_at) VALUES
('77777777-7777-7777-7777-777777777777', '66666666-6666-6666-6666-666666666666', 16.50, 'CARD', 'PAID', 'DEMO-TXN-001', NOW());

INSERT INTO parking_activities (vehicle_id, parking_slot_id, activity_type, description) VALUES
('44444444-4444-4444-4444-444444444444', 'aaaaaaaa-3333-3333-3333-aaaaaaaaaaaa', 'CHECK_IN', 'Seed check-in activity');
