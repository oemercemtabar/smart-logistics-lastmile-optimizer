CREATE UNIQUE INDEX IF NOT EXISTS uq_drivers_vehicle_id
ON drivers (vehicle_id)
WHERE vehicle_id IS NOT NULL;