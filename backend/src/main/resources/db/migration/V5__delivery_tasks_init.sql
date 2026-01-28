CREATE TABLE IF NOT EXISTS delivery_tasks (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  external_ref TEXT,

  customer_name TEXT NOT NULL,
  address TEXT NOT NULL,

  lat DOUBLE PRECISION NOT NULL,
  lng DOUBLE PRECISION NOT NULL,

  window_start TIMESTAMPTZ,
  window_end   TIMESTAMPTZ,

  service_minutes INT NOT NULL,
  priority INT NOT NULL DEFAULT 0,
  status TEXT NOT NULL DEFAULT 'PLANNED',

  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_delivery_tasks_external_ref
ON delivery_tasks (external_ref)
WHERE external_ref IS NOT NULL;

CREATE INDEX IF NOT EXISTS ix_delivery_tasks_status ON delivery_tasks (status);