import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.time.LocalTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            Document alumnosDoc = cargarXML("alumno.xml");
            Document asignaturasDoc = cargarXML("asignatura.xml");
            Document justificantesDoc = cargarXML("justificante.xml");

            NodeList alumnos = alumnosDoc.getElementsByTagName("alumno");
            System.out.println("Seleccione un alumno:");
            for (int i = 0; i < alumnos.getLength(); i++) {
                Element alumno = (Element) alumnos.item(i);
                System.out.println((i + 1) + ". " + alumno.getElementsByTagName("nombre_completo").item(0).getTextContent());
            }
            System.out.print("Número del alumno: ");
            int numAlumno = Integer.parseInt(sc.nextLine());
            Element alumnoSeleccionado = (Element) alumnos.item(numAlumno - 1);
            String idAlumno = alumnoSeleccionado.getAttribute("id");
            String nombreAlumno = alumnoSeleccionado.getElementsByTagName("nombre_completo").item(0).getTextContent();

            NodeList asignaturas = asignaturasDoc.getElementsByTagName("asignatura");
            System.out.println("Seleccione una asignatura:");
            for (int i = 0; i < asignaturas.getLength(); i++) {
                Element asig = (Element) asignaturas.item(i);
                System.out.println((i + 1) + ". " + asig.getElementsByTagName("nombre").item(0).getTextContent());
            }
            System.out.print("Número de asignatura: ");
            int numAsig = Integer.parseInt(sc.nextLine());
            Element asigSeleccionada = (Element) asignaturas.item(numAsig - 1);
            String idAsig = asigSeleccionada.getAttribute("id");
            String nombreAsig = asigSeleccionada.getElementsByTagName("nombre").item(0).getTextContent();
            String curso = asigSeleccionada.getElementsByTagName("curso").item(0).getTextContent();

            System.out.print("Ingrese la hora de la falta (HH:MM) o presione Enter para usar la hora actual: ");
            String hora = sc.nextLine();
            if (hora.isEmpty()) {
                hora = LocalTime.now().toString().substring(0, 5);
            }

            System.out.print("¿Está justificada la falta? (s/n): ");
            String justificada = sc.nextLine().trim().toLowerCase();
            String textoJustificacion = "";
            String motivo = "";
            if (justificada.equals("s")) {
                System.out.print("Escriba el texto de justificación: ");
                textoJustificacion = sc.nextLine();
                motivo = "Justificada por el tutor";
            } else {
                textoJustificacion = "Falta sin justificar";
                motivo = "No presentado justificante";
            }

            Element nuevoJustificante = justificantesDoc.createElement("justificante");
            nuevoJustificante.setAttribute("id", "J" + (justificantesDoc.getElementsByTagName("justificante").getLength() + 1));

            Element eHora = justificantesDoc.createElement("hora");
            eHora.setTextContent(hora);
            nuevoJustificante.appendChild(eHora);

            Element eAsig = justificantesDoc.createElement("asignatura_ref");
            eAsig.setTextContent(idAsig);
            nuevoJustificante.appendChild(eAsig);

            Element eAlumno = justificantesDoc.createElement("alumno_ref");
            eAlumno.setTextContent(idAlumno);
            nuevoJustificante.appendChild(eAlumno);

            Element eTexto = justificantesDoc.createElement("texto");
            eTexto.setTextContent(textoJustificacion);
            nuevoJustificante.appendChild(eTexto);

            Element ePorque = justificantesDoc.createElement("porque");
            ePorque.setTextContent(motivo);
            nuevoJustificante.appendChild(ePorque);

            Element eCurso = justificantesDoc.createElement("curso");
            eCurso.setTextContent(curso);
            nuevoJustificante.appendChild(eCurso);

            Element eJustificada = justificantesDoc.createElement("justificada");
            eJustificada.setTextContent(justificada);
            nuevoJustificante.appendChild(eJustificada);

            justificantesDoc.getDocumentElement().appendChild(nuevoJustificante);

            if (justificada.equals("n")) {
                Element ePorcentaje = (Element) alumnoSeleccionado.getElementsByTagName("porcentaje_asistencia").item(0);
                int porcentaje = Integer.parseInt(ePorcentaje.getTextContent().trim());
                porcentaje += 3;
                ePorcentaje.setTextContent(String.valueOf(porcentaje));

                Element padre = (Element) ((Element) alumnoSeleccionado.getElementsByTagName("nombres_padres").item(0))
                        .getElementsByTagName("nombre").item(0);
                String nombrePadre = padre.getTextContent();

                System.out.println("Estimado " + nombrePadre + " de " + nombreAlumno);
                System.out.println("Le informamos que a las " + hora + " en la asignatura de " + nombreAsig + " el alumno no asistió.");
                if (porcentaje > 9) {
                    System.out.println("Le avisamos que " + nombreAlumno + " está cerca de perder la extraordinaria, ya que su % es de " + porcentaje + "%.");
                }
            }

            guardarXML(alumnosDoc, "alumno.xml");
            guardarXML(justificantesDoc, "justificante.xml");

            System.out.println(" Datos actualizados correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Document cargarXML(String nombreArchivo) throws Exception {
        File file = new File(nombreArchivo);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(file);
    }

    private static void guardarXML(Document doc, String nombreArchivo) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(nombreArchivo));
        transformer.transform(source, result);
    }
}
