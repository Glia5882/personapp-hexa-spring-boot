package co.edu.javeriana.as.personapp.terminal.menu;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.domain.StudyId;
import co.edu.javeriana.as.personapp.terminal.adapter.EstudioInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.EstudioModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EstudioMenu {

    private static final int OPCION_REGRESAR_MODULOS = 0;
    private static final int PERSISTENCIA_MARIADB = 1;
    private static final int PERSISTENCIA_MONGODB = 2;
    private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
    private static final int OPCION_VER_TODO = 1;
    private static final int OPCION_VER_UNO = 2;
    private static final int OPCION_CREAR = 3;
    private static final int OPCION_EDITAR = 4;
    private static final int OPCION_ELIMINAR = 5;

    public void iniciarMenu(EstudioInputAdapterCli estudiosInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do {
            try {
                mostrarMenuMotorPersistencia();
                int opcion = leerOpcion(keyboard);
                switch (opcion) {
                    case OPCION_REGRESAR_MODULOS:
                        isValid = true;
                        break;
                    case PERSISTENCIA_MARIADB:
                        estudiosInputAdapterCli.setStudyOutputPortInjection("MARIA");
                        menuOpciones(estudiosInputAdapterCli, keyboard);
                        break;
                    case PERSISTENCIA_MONGODB:
                        estudiosInputAdapterCli.setStudyOutputPortInjection("MONGO");
                        menuOpciones(estudiosInputAdapterCli, keyboard);
                        break;
                    default:
                        log.warn("La opción elegida no es válida.");
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!isValid);
    }

    private void menuOpciones(EstudioInputAdapterCli estudiosInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
        do {
            mostrarMenuOpciones();
            int opcion = leerOpcion(keyboard);
            switch (opcion) {
                case OPCION_REGRESAR_MOTOR_PERSISTENCIA:
                    isValid = true;
                    break;
                case OPCION_VER_TODO:
                    verTodo(estudiosInputAdapterCli);
                    break;
                case OPCION_VER_UNO:
                    verUno(estudiosInputAdapterCli, keyboard);
                    break;
                case OPCION_CREAR:
                    crearEstudio(estudiosInputAdapterCli, keyboard);
                    break;
                case OPCION_EDITAR:
                    editarEstudio(estudiosInputAdapterCli, keyboard);
                    break;
                case OPCION_ELIMINAR:
                    eliminarEstudio(estudiosInputAdapterCli, keyboard);
                    break;
                default:
                    log.warn("La opción elegida no es válida.");
            }
        } while (!isValid);
    }

    private void mostrarMenuOpciones() {
        System.out.println("----------------------");
        System.out.println(OPCION_VER_TODO + " para ver todas las carreras");
        System.out.println(OPCION_VER_UNO + " para ver una carrera");
        System.out.println(OPCION_CREAR + " para crear una carrera");
        System.out.println(OPCION_EDITAR + " para editar una carrera");
        System.out.println(OPCION_ELIMINAR + " para eliminar una carrera");
        System.out.println(OPCION_REGRESAR_MOTOR_PERSISTENCIA + " para regresar");
    }
    
    private void mostrarMenuMotorPersistencia() {
        System.out.println("----------------------");
        System.out.println(PERSISTENCIA_MARIADB + " para MariaDB");
        System.out.println(PERSISTENCIA_MONGODB + " para MongoDB");
        System.out.println(OPCION_REGRESAR_MODULOS + " para regresar");
    }
    

    private void verTodo(EstudioInputAdapterCli estudiosInputAdapterCli) {
        estudiosInputAdapterCli.historial();
    }

    private void verUno(EstudioInputAdapterCli estudiosInputAdapterCli, Scanner keyboard) {
        Integer cc = leerCC(keyboard);
        Integer id = leerID(keyboard);
        StudyId studyId = new StudyId(cc, id);
        if (cc != -1 && id != -1) {
            estudiosInputAdapterCli.obtenerEstudios(studyId);
        } else {
            System.out.println("Dato incorrecto, intenta de nuevo");
        }
    }

    private void crearEstudio(EstudioInputAdapterCli estudiosInputAdapterCli, Scanner keyboard) {
        EstudioModelCli estudiosModelCli = leerEstudios(keyboard);
        estudiosInputAdapterCli.crearEstudios(estudiosModelCli);
    }

    private void editarEstudio(EstudioInputAdapterCli estudiosInputAdapterCli, Scanner keyboard) {
        EstudioModelCli estudiosModelCli = leerEstudios(keyboard);
        estudiosInputAdapterCli.editarEstudios(estudiosModelCli);
    }

    private void eliminarEstudio(EstudioInputAdapterCli estudiosInputAdapterCli, Scanner keyboard) {
        Integer cc = leerCC(keyboard);
        Integer id = leerID(keyboard);
        StudyId studyId = new StudyId(cc, id);
        if (cc != -1 && id != -1) {
            estudiosInputAdapterCli.eliminarEstudios(studyId);
        } else {
            System.out.println("Dato incorrecto, intenta de nuevo");
        }
    }

    private int leerOpcion(Scanner keyboard) {
        try {
            System.out.print("Ingrese una opción: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Solo se permiten números.");
            return leerOpcion(keyboard);
        }
    }

    private int leerCC(Scanner keyboard) {
        try {
            System.out.print("Ingrese la cedula de la persona: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Solo se permiten cadenas.");
            return -1;
        }
    }

    private int leerID(Scanner keyboard) {
        try {
            System.out.print("Ingrese el ID de la profesión: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            log.warn("Solo se permiten cadenas.");
            return -1;
        }
    }

    private EstudioModelCli leerEstudios(Scanner keyboard) {
        try {
            System.out.print("Ingrese el id de la persona: ");
            Integer cc = keyboard.nextInt();
            System.out.print("Ingrese el ID de la profesión: ");
            Integer id = keyboard.nextInt();
            System.out.print("Ingrese la fecha de graduación (dd-MM-yyyy): ");
            keyboard.nextLine();
            String fechacad = keyboard.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate fecha = LocalDate.parse(fechacad, formatter);
            System.out.print("Ingrese el nombre de la universidad: ");
            String university = keyboard.nextLine();
            return new EstudioModelCli(cc, id, fecha, university);
        } catch (InputMismatchException e) {
            log.warn("Datos ingresados incorrectos");
            return new EstudioModelCli(0, 0, null, "");
        }
    }

}
