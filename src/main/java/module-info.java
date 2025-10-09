module bd.edu.seu.gms.gms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens bd.edu.seu.gms.gms to javafx.fxml;
    opens bd.edu.seu.gms.gms.Controllers to javafx.fxml;

    exports bd.edu.seu.gms.gms;
    exports bd.edu.seu.gms.gms.Controllers;
}