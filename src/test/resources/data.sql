DELETE FROM vehicles;
DELETE FROM parking_lots;

INSERT INTO parking_lots (lot_id, location, capacity, cost_per_minute)
VALUES
('LOT-001', 'Downtown Zone A', 10, 10.0),
('LOT-002', 'Mall', 20, 20.0);

INSERT INTO vehicles (license_plate, type, owner_name )
VALUES
('ABC-123', 'CAR', 'Jose Jackson' ),
('DEF-456', 'MOTORCYCLE', 'Alex Cruz' ),
('GHI-789', 'TRUCK', 'Maria Santos' );


