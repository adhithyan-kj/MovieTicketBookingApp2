## 🎬 Project Breakdown: Advanced Movie Ticket Booking System

This application is a **Graphical Desktop Application** built in **Java Swing**, designed to demonstrate robust **Object-Oriented Programming (OOP)**, modern **User Interface (UI)** design, and **File Handling**.

---

### I. What We Used (Technologies & Libraries)

| Item | Purpose |
| :--- | :--- |
| **Core Language** | **Java** |
| **GUI Framework** | **Swing** |
| **Modern UI** | **FlatLaf** (specifically `FlatMacLightLaf` theme) to achieve the clean, high-contrast, modern look. |
| **PDF Export** | **Apache PDFBox** library to generate the official, downloadable **.pdf bill/invoice**. |
| **IDEs** | IntelliJ IDEA / NetBeans (Used for development and version control). |

---

### II. The Core OOP Structure (Classes & Concepts)

We built five core classes to define the system's structure. This is the **most important** part for the report.

| Class Name | OOP Concept | Purpose in the App |
| :--- | :--- | :--- |
| **`Seat.java`** | **Encapsulation** | Defines a single seat (ID, status). Uses **`private` fields** (`isBooked`) controlled by public methods (`book()`, `cancel()`). |
| **`Movie.java`** | **Encapsulation** | Stores movie name, time, and **price per ticket**. |
| **`Showtime.java`** | **Aggregation** | Represents a specific showing. It holds one `Movie` object and a **2D array of `Seat` objects** (`Seat[][]`). This manages the state of the theatre for that time. |
| **`BookingManager.java`** | **Abstraction / Collections** | The central data hub. Stores all showtimes and detailed movie information in **`Map`** collections for fast retrieval. |
| **`MovieGalleryFrame` / `BookingFrame`** | **Event Handling** | The graphical interfaces that respond to user actions (clicks, selection changes). |

---

### III. Key Features and Advanced Implementation

The following features make this a strong, non-trivial mini-project:

#### 1. Dynamic Graphical User Interface (GUI)

* **Split Navigation:** The app starts with a **`MovieGalleryFrame`** (the main menu with posters and details). Clicking "Book Now" closes the gallery and opens the dedicated **`BookingFrame`**. A **"Back" button** is included for seamless navigation.
* **Graphical Seats:** The seat map is generated dynamically using a **`GridLayout`** where each seat is a clickable **`JButton`**. Colors change instantly to reflect status (Available, Selected, Booked).
* **Theatre View:** A clear, light-blue **Screen Indicator panel** is fixed above the seats, ensuring the user understands the layout context.

#### 2. Advanced File I/O & System Integration

* **PDF Generation:** The app uses the **Apache PDFBox** library to structure the final bill details, including pricing, GST calculation, and formatting, then saves it as a non-editable **.pdf file**.
* **System Opener:** The program uses the **`java.awt.Desktop` API** to automatically open the generated PDF file using the user's default PDF reader immediately after saving (providing instant receipt display).

#### 3. Transaction Management

* **Real-time Cart:** The application maintains the `selectedSeats` list. The **Bill Summary** panel instantly updates the Subtotal, 18% GST, and **Total Payable** with every click.
* **State Confirmation:** When the bill is paid, the selected seats are marked permanently **"Booked"** in the `Showtime` data model, and the final receipt remains displayed in the summary panel until the user clears the cart.


## 💾 SQL Database Integration (JDBC)

This project moves beyond simple in-memory storage to satisfy the requirement for **permanent, multi-user data persistence** by integrating a **MySQL database** using the **Java Database Connectivity (JDBC)** API.

Any seat booked and paid for is immediately reflected in the central database, allowing other instances of the application to see the updated availability.

### Architecture and Data Flow

The project uses the **Data Access Object (DAO) pattern** to keep the business logic clean and separate from the SQL execution.

This feature upgrades the project for **permanent data storage** and **multi-user, shared booking** by fulfilling the syllabus requirement for database integration. It uses **MySQL** via the **Java Database Connectivity (JDBC)** API.

### Core Architecture and Data Flow

The application uses the **Data Access Object (DAO) pattern** to securely handle all database communication.

| File | Primary Role | Persistence Logic |
| :--- | :--- | :--- |
| **`DBConnection.java`** | Manages the secure, active connection to the MySQL Server. | Reads credentials from the hidden **`db_config.txt`** file. |
| **`SeatDAO.java`** | Executes all SQL commands (`SELECT`, `INSERT`). | Queries the central `seat_status` table. |
| **`Showtime.java`** | **Data Retrieval:** On initialization, calls `SeatDAO.getBookedSeats()` to check the permanent status. | Receives status from `SeatDAO`. |
| **`BookingFrame.java`** | **Booking Logic:** Attempts an `INSERT` via the DAO when the bill is paid. | Finalizes the transaction in the database. |

### Database Structure

The project relies on a single table to track permanent availability:

| Table Name | Primary Purpose | Columns (Data Stored) |
| :--- | :--- | :--- |
| **`seat_status`** | Permanently tracks reserved seats across all systems. | `seat_id` (e.g., A5) and `showtime_key` (e.g., KGF: Chapter 3 - 10:00 AM) |



