/*
Carga los botones de la barra de navegacion para acceder a las paginas
de lista de empleados, parcelas, registros de plantacion y registros
climaticos
*/
function loadNavbar() {
    $(function () {
        $("#navbarContent").load("partials/navbar/navbar.html");
    });
}