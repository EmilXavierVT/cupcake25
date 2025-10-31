# 🧁 Cupcake System



A robust, feature-rich web application to manage cupcake orders, inventory, users, and administration – all built using Java, Javalin, and a layered architecture.
## Table of Contents
- [Features](#features)
- [Architecture](#architecture)
- [Installation](#installation)
- [Usage](#usage)
- [Reporting](#reporting)
- [Contributing](#contributing)
- [Authors](#authors)
- [License](#license)

## Features
- User registration, login, and wallet management
- Cupcake custom creation (choose bottoms and icings)
- Dynamic cart/order management with discounts
- Detailed admin analytics (top users, sales, recent orders)
- Modular, layered design for maintainability

## Architecture
The system is designed according to MVC principles, split into the following layers:
- **Controllers:** Business logic for users, orders, cupcakes, cart, and admin functionality.
- **Entities:** Core data models such as `User`, , `CupcakeInOrder`, `UserDefinedCupcake`, etc. 
- **Persistence:** Data access and mapping logic, including mappers and the connection pool.
- **Config:** Application configuration (e.g., Thymeleaf templating).
- **Exception Handling:** Centralized custom exceptions for robust error reporting.

A visual overview of the project can be found in the [final_class_diagram.puml](doc/final_class_diagram.puml) within the `doc` folder along all other diagrams.
## Installation
1. **Clone the Repository:**
``` sh
    git clone https://github.com/YOUR_GITHUB_USERNAME/cupcake-system.git
    cd cupcake-system
```
1. **Set up Environment:**
    - Make sure you have Java 17 installed.
    - Configure your database connection settings as required.


## Usage
### if using duckduckGO take in count that the html and Javalin compiling causes error in the rendering through the ContextResolver a fixx is to remove all ".html" from the render() calls in the controllers

- Access the application in your browser at: `http://localhost:7000`
- Register as a user, log in, create cupcake orders, manage your cart, and check your order status.
- Admin users can view sales analytics and manage the system.

## Reporting
📄 **Rapport:**
A comprehensive final report is located in the [`doc`](doc) folder.
**Title:** Cupcake  Rapport

## Contributing
Feel free to submit issues or pull requests for improvements. All contributions are welcome!
## Authors
- Daniel Halawi
- Emil Thorsen
- Frederik Edvardsen
- Luke Persson

## License
Distributed under the MIT License. See [LICENSE](LICENSE) for details.

