USE `rqzb`;

CREATE TABLE IF NOT EXISTS persons (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  relation VARCHAR(255) NULL,
  phone VARCHAR(64) NULL,
  note TEXT NULL,
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
    ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (id),
  KEY idx_persons_name (name)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS renqing_records (
  id BIGINT NOT NULL AUTO_INCREMENT,
  type VARCHAR(32) NOT NULL,
  name VARCHAR(255) NOT NULL,
  relationship VARCHAR(255) NOT NULL,
  relationship_note VARCHAR(255) NULL,
  occasion VARCHAR(255) NOT NULL,
  amount INT NOT NULL,
  record_date DATETIME(3) NOT NULL,
  note TEXT NULL,
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (id),
  KEY idx_renqing_records_name_date (name, record_date),
  KEY idx_renqing_records_record_date (record_date),
  KEY idx_renqing_records_type (type),
  KEY idx_renqing_records_occasion (occasion)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS gift_records (
  id BIGINT NOT NULL AUTO_INCREMENT,
  person_id BIGINT NOT NULL,
  event_type VARCHAR(255) NOT NULL,
  amount INT NOT NULL,
  direction VARCHAR(32) NOT NULL,
  event_date DATETIME(3) NULL,
  note TEXT NULL,
  location VARCHAR(255) NULL,
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
    ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (id),
  KEY idx_gift_records_person_date (person_id, event_date),
  KEY idx_gift_records_event_type (event_type),
  KEY idx_gift_records_direction (direction),
  CONSTRAINT fk_gift_records_person_id
    FOREIGN KEY (person_id)
    REFERENCES persons (id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS app_settings (
  setting_key VARCHAR(128) NOT NULL,
  setting_value VARCHAR(255) NOT NULL,
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
    ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (setting_key)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

CREATE OR REPLACE VIEW v_gift_records_with_person AS
SELECT
  gr.id,
  gr.person_id,
  p.name AS person_name,
  p.relation AS person_relation,
  gr.event_type,
  gr.amount,
  gr.direction,
  gr.event_date,
  gr.note,
  gr.location,
  gr.created_at,
  gr.updated_at
FROM gift_records gr
JOIN persons p ON p.id = gr.person_id;

CREATE OR REPLACE VIEW v_renqing_yearly_summary AS
SELECT
  YEAR(record_date) AS record_year,
  SUM(CASE WHEN amount >= 0 THEN amount ELSE 0 END) AS income_amount,
  SUM(CASE WHEN amount < 0 THEN ABS(amount) ELSE 0 END) AS expense_amount,
  SUM(amount) AS balance_amount,
  COUNT(*) AS record_count
FROM renqing_records
GROUP BY YEAR(record_date);
