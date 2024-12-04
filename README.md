> #  One-To-One Chat Application

### This `one-to-one` web chat application is not designed to compete with `WhatsApp` or `Telegram`. Instead, it provides a simple means to establish text communication between two users. This app does not have a lot of features, only the basic functionalities required:  

- Signup, Login, Logout  
- Real-time sending and receiving of messages  
- Eliminated `HTTP` requests/responses  

### This `one-to-one` chat application is built using a group of ___Spring Boot starters___ such as:  

- `Spring Boot Web`  
- `MySQL`  
- [`WebSocket`](https://docs.spring.io/spring-framework/reference/web/websocket.html)  
- [`Thymeleaf`](https://www.thymeleaf.org/)  

### Requirements to run this app:  

- MySQL must be installed  
- Maven to download the required Spring Boot starters  

## Live Shots  

> ## Signup Page  

Fill in your `fullName`, `username`, `password`, and confirm the password.  
These are the only required fields to create an account and start chatting.  

The inputs are validated using the `Spring Validation Starter`:  
- No empty or null values are accepted  
- Username redundancy is checked  
- Password confirmation is required  

![Signup Page](/screenshots/1.png)  

> ## Login Page  

To access the ___Chat Page___, you need to enter your `username`  
and `password`.  

![Login Page](/screenshots/2.png)  

> ## Chat Page  

The chat page has a simple layout with the following features:  
- A list of users  
- A logout button  
- Message notifications  

![Chat Page Layout](/screenshots/3.png)  

![Message Notifications](/screenshots/4.png)  
