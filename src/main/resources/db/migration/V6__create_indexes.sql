-- ------------------------------------------------------------
-- INDEX 1
-- Acelera buscas de ocorrências por empresa e status.
-- ------------------------------------------------------------

CREATE INDEX IF NOT EXISTS idx_incident_company_status
    ON incident (company_id, status);


-- ------------------------------------------------------------
-- INDEX 2
-- Acelera buscas de coletas por cooperativa e status.
-- ------------------------------------------------------------

CREATE INDEX IF NOT EXISTS idx_collection_cooperative_status
    ON collection (cooperative_id, current_status);


-- ------------------------------------------------------------
-- INDEX 3
-- Acelera consulta do histórico de uma coleta e sua ordenação.
-- ------------------------------------------------------------

CREATE INDEX IF NOT EXISTS idx_collection_status_collection_changed_at
    ON collection_status (collection_id, changed_at DESC);


-- ------------------------------------------------------------
-- INDEX 4
-- Acelera consultas de métricas ESG por empresa e período.
-- ------------------------------------------------------------

CREATE INDEX IF NOT EXISTS idx_esg_metric_company_period
    ON esg_metric (company_id, period);