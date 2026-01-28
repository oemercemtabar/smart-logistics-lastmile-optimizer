CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS drivers (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  full_name TEXT NOT NULL,
  email TEXT UNIQUE,
  shift_start TIME NOT NULL,
  shift_end TIME NOT NULL,
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS vehicles (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  plate_number TEXT NOT NULL UNIQUE,
  capacity_parcels INT NOT NULL CHECK (capacity_parcels >= 0),
  capacity_kg INT NOT NULL CHECK (capacity_kg >= 0),
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

ALTER TABLE drivers
  ADD COLUMN IF NOT EXISTS vehicle_id UUID NULL;

ALTER TABLE drivers
  ADD CONSTRAINT fk_drivers_vehicle
  FOREIGN KEY (vehicle_id) REFERENCES vehicles(id);

CREATE TABLE IF NOT EXISTS delivery_tasks (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  external_ref TEXT,
  customer_name TEXT NOT NULL,
  address TEXT NOT NULL,
  lat DOUBLE PRECISION NOT NULL,
  lng DOUBLE PRECISION NOT NULL,
  window_start TIMESTAMPTZ,
  window_end TIMESTAMPTZ,
  service_minutes INT NOT NULL DEFAULT 3 CHECK (service_minutes >= 0),
  priority INT NOT NULL DEFAULT 0,
  status TEXT NOT NULL DEFAULT 'PLANNED',
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_delivery_tasks_status ON delivery_tasks(status);
CREATE INDEX IF NOT EXISTS idx_delivery_tasks_window_start ON delivery_tasks(window_start);

CREATE TABLE IF NOT EXISTS route_plans (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  version INT NOT NULL,
  reason TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS route_stops (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  route_plan_id UUID NOT NULL REFERENCES route_plans(id) ON DELETE CASCADE,
  driver_id UUID NOT NULL REFERENCES drivers(id),
  delivery_task_id UUID NOT NULL REFERENCES delivery_tasks(id),
  sequence INT NOT NULL CHECK (sequence >= 0),
  planned_eta TIMESTAMPTZ,
  actual_arrival TIMESTAMPTZ,
  actual_complete TIMESTAMPTZ,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_route_stops_plan_driver ON route_stops(route_plan_id, driver_id);
CREATE UNIQUE INDEX IF NOT EXISTS uq_route_stop_plan_driver_seq ON route_stops(route_plan_id, driver_id, sequence);
