CREATE TABLE Room (
  id INT PRIMARY KEY,
  number VARCHAR(20),
  type VARCHAR(50)
);

CREATE TABLE Guest (
  id INT PRIMARY KEY,
  name VARCHAR(100)
);

CREATE TABLE Reservation (
  id INT PRIMARY KEY,
  guest_id INT,
  room_id INT,
  check_in TIMESTAMP,
  check_out TIMESTAMP,
  FOREIGN KEY (guest_id) REFERENCES Guest(id),
  FOREIGN KEY (room_id) REFERENCES Room(id)
);