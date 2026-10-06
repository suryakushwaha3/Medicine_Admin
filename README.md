 <div align="center">

# 💊 MedicineAdmin

### ⚡ Modern Admin Panel for Medicine Retail Management

<p>
  <img src="https://img.shields.io/badge/Kotlin-Android-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/Jetpack%20Compose-UI-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white"/>
  <img src="https://img.shields.io/badge/Hilt-DI-FF6F00?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Retrofit-REST%20API-48B983?style=for-the-badge"/>
</p>

<p>
  <b>MedicineAdmin</b> is a modern Android administration application
  built to manage the complete medicine retail ecosystem.
</p>

<br>

<a href="#-features">Features</a> • <a href="#-services">Services</a> • <a href="#-architecture">Architecture</a> • <a href="#-tech-stack">Tech Stack</a>

</div>

---

## 🚀 Overview

**MedicineAdmin** is the administration side of a medicine retail application.

It provides a centralized platform for managing:

> 👥 Users • 💊 Products • 📦 Orders • 💰 Sales • 📊 Stock • 🔔 Notifications • 🖼️ Posters • 🏷️ Categories

The application follows a modern Android architecture with **Jetpack Compose, MVVM, Hilt, Retrofit, OkHttp, Coroutines and StateFlow**.

---

## ✨ Features

<table>
<tr>
<td align="center">👥<br><b>User Management</b></td>
<td align="center">💊<br><b>Product Management</b></td>
<td align="center">📦<br><b>Order Management</b></td>
<td align="center">💰<br><b>Sales Management</b></td>
</tr>
<tr>
<td align="center">📊<br><b>Stock Management</b></td>
<td align="center">🔔<br><b>Notifications</b></td>
<td align="center">🖼️<br><b>Poster Management</b></td>
<td align="center">🏷️<br><b>Category Management</b></td>
</tr>
</table>

---

# 🧩 Services

MedicineAdmin communicates with the backend through a structured REST API layer.

### 👥 Users

* Get all users
* Get specific user
* Update user
* Approve / block users
* Update user information

### 💊 Products

* Add product
* Get products
* Update product
* Delete product
* Product image upload
* Stock management

### 📦 Orders

* Create order
* Get all orders
* Get user orders
* Update order
* Delete order
* Approve / reject orders

### 💰 Sales

* Add sale
* Get complete sales history
* Get user sales history
* Delete sale

### 📊 Available Stock

* Add available stock
* Get available stock
* Get user stock
* Update stock
* Delete stock

### 🔔 Notifications

* Get notifications
* Get unread notifications
* Get unread count
* Mark notification as read
* Mark all as read
* Delete notification
* Delete all notifications

### 🖼️ Posters

* Add poster
* Get all posters
* Get specific poster
* Update poster
* Delete poster
* Image upload support

### 🏷️ Categories

* Add category
* Get all categories
* Get specific category
* Update category
* Delete category
* Category image upload

---

# 🏗️ Architecture

MedicineAdmin follows a clean **MVVM + Repository architecture**.

```text
                    ┌─────────────────┐
                    │  Jetpack Compose│
                    │       UI        │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │    ViewModel    │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │   Repository    │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │   ApiService    │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ Retrofit/OkHttp │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │   Backend API   │
                    └─────────────────┘
```

### 💉 Dependency Injection

```text
                 Hilt
                  │
       ┌──────────┼──────────┐
       ▼          ▼          ▼
   Retrofit    Repository  ViewModel
       │
       ▼
   ApiService
```

Dependencies are provided through Hilt instead of manually creating API, repository, or ViewModel instances.

---

# 🔄 Data Flow

```text
User Action
     ↓
Compose Screen
     ↓
ViewModel
     ↓
Repository
     ↓
ApiService
     ↓
Retrofit + OkHttp
     ↓
Backend
     ↓
API Response
     ↓
Repository
     ↓
ViewModel
     ↓
StateFlow
     ↓
Compose UI
```

---

# 🛠️ Tech Stack

<div align="center">

|          Technology         | Purpose               |
| :-------------------------: | :-------------------- |
|        🟣 **Kotlin**        | Primary language      |
|    🎨 **Jetpack Compose**   | Modern UI             |
|      🧩 **Material 3**      | UI components         |
|         🏗️ **MVVM**        | Architecture          |
|         💉 **Hilt**         | Dependency Injection  |
|       🌐 **Retrofit**       | API communication     |
|        🔌 **OkHttp**        | Network client        |
|       ⚡ **Coroutines**      | Async operations      |
|       🔄 **StateFlow**      | Reactive state        |
| 📦 **Kotlin Serialization** | Data serialization    |
|       🔗 **REST API**       | Backend communication |

</div>

---

# 📂 Project Structure

```text
com.example.medicineadmin
│
├── data
│   └── ApiService
│
├── di
│   └── AppModule
│
├── model
│   ├── User
│   ├── Product
│   ├── Order
│   ├── Sale
│   ├── Notification
│   ├── AvailableStock
│   ├── Poster
│   └── Category
│
├── repository
│
├── view
│   ├── Screen
│   └── Components
│
├── viewModel
│
├── MainActivity
│
└── MedicineAdmin
```

---

# 🌐 API Modules

The API layer is organized around independent business modules:

```text
API
│
├── 👥 Users
├── 💊 Products
├── 📦 Orders
├── 💰 Sales
├── 📊 Available Stock
├── 🔔 Notifications
├── 🖼️ Posters
└── 🏷️ Categories
```

This modular structure makes it easier to extend the application with new services in the future.

---

# 🎯 Goals

* Build a professional medicine administration platform
* Keep the architecture clean and scalable
* Provide centralized pharmacy management
* Make API integration easy to maintain
* Support future modules without major architectural changes
* Deliver a modern Android admin experience

---

# 🚧 Development Status

<div align="center">

### 🟡 Active Development

New screens, API integrations and management features are being added progressively.

</div>

---

# 🔮 Future Plans

* 🔐 Admin Authentication
* 📈 Advanced Analytics
* 📊 Sales Dashboard
* 🔔 Real-time Notifications
* 🧾 Invoice Management
* 👤 Role-Based Admin Access
* 📦 Advanced Inventory System
* 🌙 Complete Dark Theme
* 📤 Export Reports

---

<div align="center">

## 💊 MedicineAdmin

**Manage • Monitor • Control**

Built with ❤️ using modern Android development practices.

<br>

⭐ **Star this repository if you like the project!**

</div>
