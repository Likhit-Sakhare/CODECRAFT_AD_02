# **TodoList**

This task by CODECRAFT INFOTECH is a todoList app where user can save a todo, set color for that todo and also sort them according to their choice.

---

## **Features**

#### **Create a Todo**
  - Set title (mandatory) and description (optional).
  - Choose a color from the color bar or select no color, at-first color is randomly assigned.
  - When you add a numbered bullet point e.g., "1." in the description, then pressing "Enter" will automatically adds the next numbered bullet point.
#### **Edit a Todo**
  - Modify the title, description, and color of an existing todo.
#### **Mark as Done/Undone**
  - When you mark a todo as done, a strike-through effect appears on the text.
  - Unmark a todo to revert its appearance.
#### **Sorting, Delete and Undo**
  - Sort the todos based on their Title, Date and color, you can do that either ascending or descending.
  - Swipe to delete a todo.
  - A message appears with an "Undo" option to restore the deleted todo.

---

## **Demo**

<video src="https://github.com/user-attachments/assets/074f1137-8c08-4997-8e50-e35903b62b53" controls="controls" style="max-width: 100%; height: auto;">
    Demo how the app works.
</video>

---

## **Libraries and Methods Used**

1. **Kotlin**: First-class and official programming language for Android development.
2. **Jetpack compose**: A toolkit for building Android apps that uses a declarative API to simplify and speed up UI development
3. **Material Components for Android**: For modular and customizable Material Design UI components.
4. **MVVM**: It is an architectural pattern that separates UI (View) from business logic (ViewModel) and data handling (Model) to improve maintainability and testability.
5. **Clean Architecture**: It a software design pattern that separates concerns by organizing code into layers (e.g., presentation, domain, data) to enhance maintainability, scalability, and testability.
6. **Kotlin Coroutines**: They are a concurrency framework that simplifies asynchronous programming by allowing tasks to be written sequentially while managing threading and suspensions efficiently.
7. **Dagger Hilt**: It is a dependency injection library for Android that simplifies providing and managing dependencies across the app's lifecycle.
8. **Room**: A persistence library that provides an abstraction layer over SQLite to manage local database operations efficiently in Android applications.
9. **Regex**: It is a powerful pattern-matching technique used to search, validate, and manipulate text based on specific rules.
10. **Splash API**: The Splash API in Android provides a customizable launch screen with a smooth transition into the app using the SplashScreen class.
11. **Jetpack Compose Animation APIs**: It is used for smooth UI transitions with animate*AsState, AnimatedVisibility, and updateTransition to enhance user experience.

---

## Lessons Learned

#### **Swipe to Delete using SwipeToDismiss**
  - Learned how to implement swipe-to-delete functionality using Jetpack Compose’s SwipeToDismiss from Material 3.
  - Understood what is dismissState, what is SwipeToDismissBox and how to customize the background while swiping.

#### **Regex, its usage and numbered bullet points**
  - Learned about Regular Expression (Regex) and how they help in pattern matching.
  - Used Regex to detect the numbered bullet point's pattern in the description.
  - Learned how to implement automatic bullet point generation, where pressing Enter after a numbered line (1.) automatically adds the next (2.).

#### **Importance of providing a Unique Key in LazyColumn**
  - Encountered a UI issue where deleting a todo makes the todos below that deleted that todo disappear from the UI.
  - Realized that without a unique key, Jetpack Compose reuses UI elements in LazyColumn, leading to inconsistencies.
  - Solved the issue by assigning the id of each todo as the key, this ensured that deleting a specific todo only affected the intended item.

---

## **Contact**
For any questions or feedback, feel free to contact me at sakhare1181likhit@gmail.com and also connect with me on LinkedIn at www.linkedin.com/in/likhit-sakhare and on Twitter at https://x.com/likhit_sakhare
