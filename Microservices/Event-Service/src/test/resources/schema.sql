CREATE TABLE IF NOT EXISTS events (
                                      id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                      event_name VARCHAR(255),
    description TEXT,
    location VARCHAR(255),
    date TIMESTAMP,
    available_tickets INT,
    ticket_price DECIMAL(10,2),
    is_hot_event BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
