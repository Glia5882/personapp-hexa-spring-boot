package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profesion;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfesionInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProfesionMenu {

    private static final int OPCION_REGRESAR_MODULOS = 0;
    private static final int PERSISTENCIA_MARIADB = 1;
    private static final int PERSISTENCIA_MONGODB = 2;

    private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
    private static final int OPCION_VER_TODO = 1;
    private static final int OPCION_CREAR_PROFESION= 2;
	private static final int OPCION_ELIMINAR_PROFESION = 3;
	private static final int OPCION_EDITAR_PROFESION = 4;

    public void iniciarMenu(ProfesionInputAdapterCli profesionInputAdapterCli, Scanner keyboard) {
       

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
                    profesionInputAdapterCli.setProfesionOutputPortInjection("MARIA");
                    menuOpciones(profesionInputAdapterCli, keyboard);
                    break;
                case PERSISTENCIA_MONGODB:
                    profesionInputAdapterCli.setProfesionOutputPortInjection("MONGO");
                    menuOpciones(profesionInputAdapterCli, keyboard);
                    break;
                default:
                    log.warn("La opción elegida no es válida.");
                }
            } catch (InvalidOptionException e) {
                log.warn(e.getMessage());
            }
        } while (!isValid);
    }

    private void menuOpciones(ProfesionInputAdapterCli profesionInputAdapterCli, Scanner keyboard) {
        boolean isValid = false;
		do {
			try {
				mostrarMenuOpciones();
				int opcion = leerOpcion(keyboard);
				switch (opcion) {
					case OPCION_REGRESAR_MODULOS:
						isValid = true;
						break;
					case OPCION_VER_TODO:
                    profesionInputAdapterCli.historial();
						break;
					case OPCION_CREAR_PROFESION:
						crearProfesion(profesionInputAdapterCli, keyboard);
						break;
					case OPCION_ELIMINAR_PROFESION:
						eliminarProfesion( profesionInputAdapterCli, keyboard);
						break;
					case OPCION_EDITAR_PROFESION:
						editarProfesion( profesionInputAdapterCli, keyboard);
						break;
					default:
						log.warn("La opción elegida no es válida.");
				}
			} catch (InputMismatchException | NoExistException e) {
				log.warn("Solo se permiten números.");
			}
		} while (!isValid);
    }

    

    private void mostrarMenuOpciones() {
            System.out.println("----------------------");
            System.out.println(OPCION_VER_TODO + " para ver todas las profesiones");
            System.out.println(OPCION_CREAR_PROFESION + " para crear una nueva profesion");
            System.out.println(OPCION_ELIMINAR_PROFESION+ " para eliminar una profesion");
            System.out.println(OPCION_EDITAR_PROFESION+ " para editar una profesion");
            System.out.println(OPCION_REGRESAR_MOTOR_PERSISTENCIA + " para regresar");
        }
    

    private void mostrarMenuMotorPersistencia() {
        System.out.println("----------------------");
        System.out.println(PERSISTENCIA_MARIADB + " para MariaDB");
        System.out.println(PERSISTENCIA_MONGODB + " para MongoDB");
        System.out.println(OPCION_REGRESAR_MODULOS + " para regresar");
    }

    private int leerOpcion(Scanner keyboard) {
        try {
            System.out.print("Ingrese una opción: ");
            return keyboard.nextInt();
        } catch (InputMismatchException e) {
            keyboard.next();  
            log.warn("Solo se permiten números.");
            return leerOpcion(keyboard);
        }
    }

    private void crearProfesion(ProfesionInputAdapterCli profesionInputAdapterCli, Scanner keyboard) {
    log.info("Creación de una nueva profesión");
    keyboard.nextLine();  
    System.out.print("Ingrese el id de la profesion: ");
    String idStr = keyboard.nextLine();
    Integer id = null;
    try {
        id = Integer.parseInt(idStr);  
    } catch (NumberFormatException e) {
        log.error("El ID ingresado no es un número válido. Por favor, ingrese un número entero.");
        return; 
    }

    System.out.print("Ingrese el nombre de la profesión: ");
    String nombre = keyboard.nextLine();
    System.out.print("Ingrese la descripción de la profesión: ");
    String descripcion = keyboard.nextLine();

    Profesion profesion = new Profesion();
    profesion.setId(id);
    profesion.setNom(nombre);
    profesion.setDes(descripcion);
    

    if (profesionInputAdapterCli.create(profesion))
        System.out.println("SE CREÓ LA NUEVA PROFESIÓN DE MANERA CORRECTA!");
    else
        System.out.println("NO SE PUDO CREAR LA PROFESIÓN");
    
}

private void eliminarProfesion(ProfesionInputAdapterCli profesionInputAdapterCli, Scanner keyboard) {
    System.out.print("Ingrese el ID de la profesión que desea eliminar: ");
    keyboard.nextLine();
    Integer id = keyboard.nextInt();
    try {
        profesionInputAdapterCli.drop(id);
        System.out.println("Profesión eliminada correctamente.");
    } catch (NoExistException e) {
        System.out.println(e.getMessage());
    }
}

private void editarProfesion(ProfesionInputAdapterCli profesionInputAdapterCli, Scanner keyboard) throws NoExistException {
    log.info("Edición de una profesión");
    System.out.print("Ingrese el ID de la profesión que desea editar: ");
    Integer idProfesion = keyboard.nextInt();
    keyboard.nextLine(); 
    System.out.print("Ingrese el nuevo nombre de la profesión: ");
    String nombre = keyboard.nextLine();
    System.out.print("Ingrese la nueva descripción de la profesión: ");
    String descripcion = keyboard.nextLine();

    Profesion profesion = new Profesion();
    profesion.setNom(nombre);
    profesion.setDes(descripcion);
    profesion.setId(idProfesion);

    if (profesionInputAdapterCli.edit(idProfesion, profesion))
        System.out.println("Se pudo editar la profesión");
    else
        System.out.println("No se pudo editar la profesión");
}


}


