module com.mycompany.capstone.project {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.capstone.project to javafx.fxml;
    exports com.mycompany.capstone.project;
}
