-- ============================================================
-- 3. TRIGGER - INCIDENT
-- ============================================================

DROP TRIGGER IF EXISTS trg_audit_incident ON incident;

CREATE TRIGGER trg_audit_incident
    AFTER INSERT OR
UPDATE OR
DELETE
ON incident
    FOR EACH ROW
    EXECUTE FUNCTION audit_changes();


-- ============================================================
-- 4. TRIGGER - COLLECTION
-- ============================================================

DROP TRIGGER IF EXISTS trg_audit_collection ON collection;

CREATE TRIGGER trg_audit_collection
    AFTER INSERT OR
UPDATE OR
DELETE
ON collection
    FOR EACH ROW
    EXECUTE FUNCTION audit_changes();


-- ============================================================
-- 5. TRIGGER - ESG_METRIC
-- ============================================================

DROP TRIGGER IF EXISTS trg_audit_esg_metric ON esg_metric;

CREATE TRIGGER trg_audit_esg_metric
    AFTER INSERT OR
UPDATE OR
DELETE
ON esg_metric
    FOR EACH ROW
    EXECUTE FUNCTION audit_changes();
