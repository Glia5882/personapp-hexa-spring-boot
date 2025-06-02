package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.adapter.PersonaInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonaMenu {

	private static final int OPCION_REGRESAR_MODULOS = 0;
	private static final int PERSISTENCIA_MARIADB = 1;
	private static final int PERSISTENCIA_MONGODB = 2;

	private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
	private static final int OPCION_VER_TODO = 1;
	private static final int OPCION_CREAR_PERSONA = 2;
	private static final int OPCION_ELIMINAR_PERSONA = 3;
	private static final int OPCION_EDITAR_PERSONA = 4;


	public void iniciarMenu(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
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
					personaInputAdapterCli.setPersonOutputPortInjection("MARIA");
					menuOpciones(personaInputAdapterCli,keyboard);
					break;
				case PERSISTENCIA_MONGODB:
					personaInputAdapterCli.setPersonOutputPortInjection("MONGO");
					menuOpciones(personaInputAdapterCli,keyboard);
					break;
				default:
					log.warn("La opción elegida no es válida.");
				}
			}  catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
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
						personaInputAdapterCli.historial();
						break;
					case OPCION_CREAR_PERSONA:
						crearPersona(personaInputAdapterCli, keyboard);
						break;
					case OPCION_ELIMINAR_PERSONA:
						eliminarPersona(personaInputAdapterCli, keyboard);
						break;
					case OPCION_EDITAR_PERSONA:
						editarPersona(personaInputAdapterCli, keyboard);
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
		System.out.println(OPCION_VER_TODO + " para ver todas las personas");
		System.out.println(OPCION_CREAR_PERSONA + " para crear una nueva persona");
		System.out.println(OPCION_ELIMINAR_PERSONA + " para eliminar una persona");
		System.out.println(OPCION_EDITAR_PERSONA + " para editar una persona");
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
			log.warn("Solo se permiten números.");
			return leerOpcion(keyboard);
		}
	}

	private void crearPersona(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
		log.info("Creación de una nueva persona");
		keyboard.nextLine();
		System.out.print("Ingrese el nombre de la persona: ");
		String nombre = keyboard.nextLine();
		System.out.print("Ingrese el apellido de la persona: ");
		String apellido = keyboard.nextLine();
		System.out.print("Ingrese la edad de la persona: ");
		int edad = keyboard.nextInt();
		System.out.println("Ingrese el ID de la persona: ");
		Integer cc = keyboard.nextInt();
		Gender gender = pedirGenero(keyboard);
		Person person = new Person();
		person.setIdentification(cc);
		person.setFirstName(nombre);
		person.setLastName(apellido);
		person.setGender(gender);
		person.setAge(edad);

		if (personaInputAdapterCli.create(person))
			System.out.println("SE CREO LA NUEVA PERSONA DE MANERA CORRECTA!");
		else
			System.out.println("NO SE PUDO CREAR LA PERSONA");
	}

	private void eliminarPersona(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) throws NoExistException {
		log.info("Eliminación de una persona");
		System.out.print("Ingrese el ID de la persona que desea eliminar: ");
		Integer idPersona = keyboard.nextInt();

		if (personaInputAdapterCli.drop(idPersona)) {
            System.out.println("SE ELIMINO DE MANERA CORRECTA A LA PERSONA!");
        }
			throw new NoExistException(
					"The person with id does not exist into db, cannot be dropped");
	}

	private void editarPersona(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) throws NoExistException {
		log.info("Edición de una persona");
		System.out.print("Ingrese el ID de la persona que desea editar: ");
		int idPersona = keyboard.nextInt();
		keyboard.nextLine();
		System.out.print("Ingrese el nuevo nombre de la persona: ");
		String nombre = keyboard.nextLine();
		System.out.print("Ingrese el nuevo apellido de la persona: ");
		String apellido = keyboard.nextLine();
		System.out.print("Ingrese la nueva edad de la persona: ");
		int edad = keyboard.nextInt();
		Gender gender = pedirGenero(keyboard);

		Person person = new Person();
		person.setIdentification(idPersona);
		person.setFirstName(nombre);
		person.setLastName(apellido);
		person.setAge(edad);
		person.setGender(gender);

		if (personaInputAdapterCli.edit(idPersona, person))
			System.out.println("Se pudo editar a la persona");
		else
			System.out.println("No se pudo editar a la persona");
	}

	private Gender pedirGenero(Scanner keyboard) {
		System.out.println("Seleccione el género:");
		System.out.println("1. Masculino");
		System.out.println("2. Femenino");
		System.out.println("3. Otro");
		System.out.print("Ingrese el número correspondiente al género: ");
		int opcion = keyboard.nextInt();
		switch (opcion) {
			case 1:
				return Gender.MALE;
			case 2:
				return Gender.FEMALE;
			case 3:
				return Gender.OTHER;
			default:
				System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
				return pedirGenero(keyboard);
		}
	}
}
