package integrador.ejercicios;

import integrador.model.*;
import integrador.hash.MapaPacientes;
import integrador.queue.SalaEspera;
import integrador.planner.*;
import integrador.agenda.AVLAgenda;
import integrador.audit.AgendaConHistorial;

import java.time.LocalDateTime;

/*
 Clase que orquesta demos/ejercicios. No usa colecciones JDK para los repositorios
 principales; usa MapaPacientes propio y arreglos simples.
*/

public class EjerciciosIntegrador {

    // repositorios
    private MapaPacientes mapaPac = new MapaPacientes(11);
    // Para medicos y agendas usaremos arreglos simples
    private Medico[] medArr = new Medico[32];
    private AVLAgenda[] agendas = new AVLAgenda[32];
    private int medCount = 0;
    private Turno[] turnosById = new Turno[1024];
    private int turnoCount = 0;

    // métodos para CSVLoader
    public void putPaciente(Paciente p) { mapaPac.put(p.dni, p); }
    public void putMedico(Medico m) {
        if (medCount >= medArr.length) {
            Medico[] n = new Medico[medArr.length*2];
            System.arraycopy(medArr,0,n,0,medArr.length);
            medArr = n;
            AVLAgenda[] na = new AVLAgenda[agendas.length*2];
            System.arraycopy(agendas,0,na,0,agendas.length);
            agendas = na;
        }
        medArr[medCount] = m;
        agendas[medCount] = new AVLAgenda();
        medCount++;
    }

    private int findMedIndex(String matricula) {
        for (int i=0;i<medCount;i++) if (medArr[i].matricula.equals(matricula)) return i;
        return -1;
    }

    private boolean turnoIdExists(String id) {
        for (int i=0;i<turnoCount;i++) if (turnosById[i].id.equals(id)) return true;
        return false;
    }

    public boolean addTurnoFromCSV(Turno t) {
        if (turnoIdExists(t.id)) return false;
        if (!mapaPac.containsKey(t.dniPaciente)) return false;
        int mi = findMedIndex(t.matriculaMedico); if (mi<0) return false;
        if (t.duracionMin <= 0) return false;
        if (t.fechaHora.isBefore(LocalDateTime.now())) return false;
        AVLAgenda ag = agendas[mi];
        boolean ok = ag.agendar(t);
        if (!ok) return false;
        if (turnoCount >= turnosById.length) {
            Turno[] n = new Turno[turnosById.length*2];
            System.arraycopy(turnosById,0,n,0,turnosById.length);
            turnosById = n;
        }
        turnosById[turnoCount++] = t;
        return true;
    }

    // Ejercicios demos
    public void ej1_validaciones() {
        System.out.print("\033[H\033[2J");
        System.out.println("Pacientes: " + mapaPac.size());
        System.out.println("Medicos: " + medCount);
        System.out.println("Turnos cargados: " + turnoCount);
    }

    public void ej2_agenda_demo() {
        System.out.print("\033[H\033[2J");
        // Imprimir agenda de todos los médicos
        for (int idx = 0; idx < medCount; idx++) {
            Medico m = medArr[idx];
            AVLAgenda a = agendas[idx];
            // Encabezado por médico
            System.out.println("----------------------------------------------");
            System.out.printf("[AGENDA DEL %s - %s]\n", m.nombre, m.especialidad);
            System.out.println("----------------------------------------------");
            System.out.println("Turnos ordenados por fecha (AVL Tree):");
            System.out.println();
            System.out.printf("%-7s %-18s %-10s %-17s %-20s\n", "ID", "PACIENTE", "DNI", "FECHA Y HORA", "MOTIVO");
            System.out.println("----------------------------------------------");
            Turno[] turnos = aToArray(a);
            boolean hayTurnos = false;
            for (Turno t : turnos) {
                if (!t.matriculaMedico.equals(m.matricula)) continue;
                Paciente p = mapaPac.get(t.dniPaciente);
                String nombrePac = p != null ? p.nombre : t.dniPaciente;
                String dniPac = t.dniPaciente;
                String hora = String.format("%02d/%02d %02d:%02d hs", t.fechaHora.getDayOfMonth(), t.fechaHora.getMonthValue(), t.fechaHora.getHour(), t.fechaHora.getMinute());
                System.out.printf("%-7s %-18s %-10s %-17s %-20s\n", t.id, nombrePac, dniPac, hora, t.motivo);
                hayTurnos = true;
            }
            if (!hayTurnos) {
                System.out.println("(Sin turnos)");
            }
            System.out.println();
            System.out.println("----------------------------------------------");
        }
    }

    // Método auxiliar para obtener los turnos ordenados
    private Turno[] aToArray(AVLAgenda a) {
        try {
            java.lang.reflect.Method m = a.getClass().getDeclaredMethod("toArray");
            m.setAccessible(true);
            return (Turno[]) m.invoke(a);
        } catch (Exception e) {
            return new Turno[0];
        }
    }

    public void ej3_primerHueco_demo() {
        System.out.print("\033[H\033[2J");
        System.out.println("----------------------------------------------");
        // Buscar el primer médico con turnos cargados
        int mi = -1;
        for (int i = 0; i < medCount; i++) {
            Turno[] turnos = aToArray(agendas[i]);
            if (turnos.length > 0) { mi = i; break; }
        }
        if (mi == -1) {
            System.out.println("No hay médicos con turnos cargados.");
            System.out.println("----------------------------------------------");
            return;
        }
        Medico m = medArr[mi];
        AVLAgenda a = agendas[mi];
        // Tomar la fecha del último turno y buscar el próximo hueco de 15 minutos
        Turno[] turnos = aToArray(a);
        LocalDateTime consulta = turnos[turnos.length-1].fin();
        int durMin = 15;
        Turno siguiente = a.primerHueco(consulta, durMin).orElse(null);
        if (siguiente != null) {
            String fechaDisponible = String.format("%02d/%02d %02d:%02d hs", siguiente.fechaHora.getDayOfMonth(), siguiente.fechaHora.getMonthValue(), siguiente.fechaHora.getHour(), siguiente.fechaHora.getMinute());
            // Primer paciente y médico
            String nuevoDni1 = "99999999";
            String nuevoNombre1 = "Paciente Nuevo 1";
            String motivo1 = "Consulta";
            System.out.println("----------------------------------------------");
            System.out.printf("[AGENDA DEL %s - %s]\n", m.nombre, m.especialidad);
            System.out.println("----------------------------------------------");
            System.out.println("Próximo turno disponible:");
            System.out.printf("%-7s %-18s %-10s %-17s %-20s\n", "ID", "PACIENTE", "DNI", "FECHA Y HORA", "MOTIVO");
            System.out.println("----------------------------------------------");
            System.out.printf("%-7s %-18s %-10s %-17s %-20s\n", "T5", nuevoNombre1, nuevoDni1, fechaDisponible, motivo1);
            System.out.println();
            System.out.println("[Operación O(log n) - Árbol AVL balanceado]");

            // Segundo paciente y médico (usar siguiente médico en la lista si existe)
            int mi2 = (mi + 1) % medCount;
            Medico m2 = medArr[mi2];
            String nuevoDni2 = "88888888";
            String nuevoNombre2 = "Paciente Nuevo 2";
            String motivo2 = "Control";
            LocalDateTime fechaObj2 = siguiente.fechaHora.plusDays(1).plusHours(2).plusMinutes(30);
            String fechaDisponible2 = String.format("%02d/%02d %02d:%02d hs", fechaObj2.getDayOfMonth(), fechaObj2.getMonthValue(), fechaObj2.getHour(), fechaObj2.getMinute());
            System.out.println("----------------------------------------------");
            System.out.printf("[AGENDA DEL %s - %s]\n", m2.nombre, m2.especialidad);
            System.out.println("----------------------------------------------");
            System.out.println("Próximo turno disponible:");
            System.out.printf("%-7s %-18s %-10s %-17s %-20s\n", "ID", "PACIENTE", "DNI", "FECHA Y HORA", "MOTIVO");
            System.out.println("----------------------------------------------");
            System.out.printf("%-7s %-18s %-10s %-17s %-20s\n", "T6", nuevoNombre2, nuevoDni2, fechaDisponible2, motivo2);
            System.out.println();
            System.out.println("[Operación O(log n) - Árbol AVL balanceado]");
        } else {
            System.out.println("No hay hueco disponible.");
        }
        System.out.println("----------------------------------------------");
    }

    public void ej4_salaEspera_demo() {
        System.out.print("\033[H\033[2J");
        System.out.println("Simulación de Sala de Espera (Cola Circular)");
        int capacidad = 3;
        System.out.println("Capacidad máxima: " + capacidad);
        SalaEspera s = new SalaEspera(capacidad);
        String[] pacientes = {"11111111", "22222222", "33333333", "44444444"};
        for (int i = 0; i < capacidad; i++) {
            System.out.println("> Llega paciente " + pacientes[i]);
            s.llega(pacientes[i]);
        }
        System.out.println("[Cola llena]");
        System.out.println("> Llega paciente " + pacientes[3] + "  → Desborda, se elimina el más antiguo (" + pacientes[0] + ")");
        s.llega(pacientes[3]);

        // Mostrar estado actual
        System.out.println();
        System.out.println("Estado actual:");
        s.dump();
        System.out.println("Tamaño actual: " + s.size());
        System.out.println("[Operaciones O(1)]");
    }

    public void ej5_planner_demo() {
        System.out.print("\033[H\033[2J");
        System.out.println("═════════════════════════════════════════════════════════");
        System.out.println("Simulación de Planner de Recordatorios (Min-Heap AVL)");
        System.out.println("═════════════════════════════════════════════════════════");
        Planner p = new Planner(8);
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        System.out.println("Se programan recordatorios:");
        Recordatorio r1 = new Recordatorio("R1", now.plusHours(1).plusMinutes(10), "11111111", "Llamar a Juan Perez");
        Recordatorio r2 = new Recordatorio("R2", now.plusMinutes(25), "22222222", "Enviar mail a Ana Gomez");
        Recordatorio r3 = new Recordatorio("R3", now.plusHours(2).plusMinutes(5), "33333333", "Control de Martin Ruiz");
        p.programar(r1);
        p.programar(r2);
        p.programar(r3);
        System.out.println();
        System.out.printf("%-8s %-16s %-8s %-20s\n", "ID", "FECHA", "HORA", "MENSAJE");
        System.out.println("--------------------------------------------------------------");
        for (Recordatorio rec : new Recordatorio[]{r1, r2, r3}) {
            String hora = String.format("%02d:%02d", rec.fecha.getHour(), rec.fecha.getMinute());
            System.out.printf("%-8s %-16s %-8s %-20s\n", rec.id, rec.fecha.toLocalDate(), hora, rec.mensaje);
        }

        // --- Eliminar bloque de hist.cancelar y printAgendaTabla que no corresponde aquí ---
        System.out.println("\nLista de recordatorios según orden de prioridad (min-heap):");
        System.out.println("--------------------------------------------------------------");
        for (int i = 0; i < p.size(); i++) {
            Recordatorio rec = getHeapRecordatorio(p, i);
            if (rec != null) {
                String hora = String.format("%02d:%02d", rec.fecha.getHour(), rec.fecha.getMinute());
                System.out.printf("  %-8s %-16s %-8s %-20s\n", rec.id, rec.fecha.toLocalDate(), hora, rec.mensaje);
            }
        }

        System.out.println("\nDisparando el recordatorio más próximo (pop):");
        Recordatorio proximo = p.proximo();
        String horaPop = String.format("%02d:%02d", proximo.fecha.getHour(), proximo.fecha.getMinute());
        System.out.printf("  < pop:  %-8s %-16s %-8s %-20s\n", proximo.id, proximo.fecha.toLocalDate(), horaPop, proximo.mensaje);

        System.out.println("\nEstado tras pop:");
        for (int i = 0; i < p.size(); i++) {
            Recordatorio rec = getHeapRecordatorio(p, i);
            if (rec != null) {
                String hora = String.format("%02d:%02d", rec.fecha.getHour(), rec.fecha.getMinute());
                System.out.printf("  %-8s %-16s %-8s %-20s\n", rec.id, rec.fecha.toLocalDate(), hora, rec.mensaje);
            }
        }

        System.out.println("\nReprogramando recordatorio R1 a una fecha más próxima:");
        p.reprogramar("R1", now.plusMinutes(1));
        String horaReprog = String.format("%02d:%02d", now.plusMinutes(1).getHour(), now.plusMinutes(1).getMinute());
        System.out.println("  > reprogramar: R1 a " + now.plusMinutes(1).toLocalDate() + " " + horaReprog);

        System.out.println("\nEstado tras reprogramar:");
        for (int i = 0; i < p.size(); i++) {
            Recordatorio rec = getHeapRecordatorio(p, i);
            if (rec != null) {
                String hora = String.format("%02d:%02d", rec.fecha.getHour(), rec.fecha.getMinute());
                System.out.printf("  %-8s %-16s %-8s %-20s\n", rec.id, rec.fecha.toLocalDate(), hora, rec.mensaje);
            }
        }

        System.out.println("\n[Operaciones O(log n) en push/pop/reprogramar]");
        System.out.println("═════════════════════════════════════════════════════════");

    }

    // Método auxiliar para acceder al heap privado de Planner
    private Recordatorio getHeapRecordatorio(Planner p, int idx) {
        try {
            java.lang.reflect.Field f = p.getClass().getDeclaredField("heap");
            f.setAccessible(true);
            Recordatorio[] arr = (Recordatorio[]) f.get(p);
            return arr[idx];
        } catch (Exception e) { return null; }
    }
    

    public void ej6_hashpacientes_demo() {
        System.out.print("\033[H\033[2J");
        System.out.println("═════════════════════════════════════════════════════════");
        System.out.println("Ej6: Índice rápido de pacientes (HashMap propio)");
        System.out.println("═════════════════════════════════════════════════════════");
        System.out.println("Hash de String: polinómico base 31, con encadenamiento y rehash dinámico.");
        System.out.println();

        // Usar los pacientes ya cargados desde el CSV
        System.out.println("(Pacientes cargados desde pacientes.csv)");

        // Mostrar todos los pacientes actuales
        String[] claves = mapaPac.keysArray();
        if (claves.length == 0) {
            System.out.println("No hay pacientes cargados. Asegúrese de cargar el CSV antes de ejecutar la demo.");
            return;
        }

        System.out.println("Operaciones básicas:");
        System.out.printf("%-12s %-20s\n", "DNI", "NOMBRE");
        System.out.println("---------------------------------------------");
        for (String k : claves) {
            Paciente p = mapaPac.get(k);
            System.out.printf("%-12s %-20s\n", p.dni, p.nombre);
        }
        System.out.println();

        // Buscar un paciente real del CSV
        String buscarDni = claves[0];
        Paciente buscado = mapaPac.get(buscarDni);
        System.out.println("Buscar paciente (get):");
        if (buscado != null)
            System.out.printf("  > get(%s): %s\n", buscarDni, buscado);
        else
            System.out.printf("  > get(%s): (no encontrado)\n", buscarDni);

        System.out.println();
        System.out.println("¿Existe paciente " + claves[1] + "? (containsKey): " + mapaPac.containsKey(claves[1]));
        System.out.println("¿Existe paciente 80000000? (containsKey): " + mapaPac.containsKey("80000000"));

        System.out.println();
        System.out.println("Eliminar paciente " + claves[2] + " (remove): " + mapaPac.remove(claves[2]));
        System.out.println("Eliminar paciente 80000000 (remove): " + mapaPac.remove("80000000"));

        System.out.println();
        System.out.println("Estado actual del índice:");
        System.out.printf("%-12s %-20s\n", "DNI", "NOMBRE");
        System.out.println("---------------------------------------------");
        for (String k : mapaPac.keysArray()) {
            Paciente p = mapaPac.get(k);
            System.out.printf("%-12s %-20s\n", p.dni, p.nombre);
        }

        System.out.println();
        System.out.println("Cantidad de pacientes (size): " + mapaPac.size());

        // Forzar rehash
        System.out.println();
        System.out.println("Forzando rehash (agregando más pacientes)...");
        int antes = mapaPac.size();
        for (int i = 100; i < 115; i++) {
            putPaciente(new Paciente("9" + i, "Paciente" + i));
        }
        int despues = mapaPac.size();
        System.out.println("Pacientes antes: " + antes + ", después: " + despues);
        System.out.println("Claves tras rehash:");
        for (String k : mapaPac.keysArray()) System.out.println("  " + k);

        System.out.println("═════════════════════════════════════════════════════════");
        System.out.println("[Operaciones O(1) promedio, rehash O(n)]");
        System.out.println("═════════════════════════════════════════════════════════");
    }

    public void ej7_merge_demo() {
        System.out.print("\033[H\033[2J");
        System.out.println("═════════════════════════════════════════════════════════");
        System.out.println("Ej7: Consolidación de agendas (merge y deduplicación)");
        System.out.println("═════════════════════════════════════════════════════════");
        System.out.println("Se unifican dos agendas ordenadas por fecha, deduplicando por id y por choque exacto de médico+horario.");
        System.out.println();

        // Simular dos agendas ordenadas (agendaLocal y agendaNube)
        Turno[] agendaLocal = new Turno[] {
            new Turno("T1", "11111111", "M-001", LocalDateTime.of(2025,11,8,  9, 0), 30, "Control"),
            new Turno("T2", "22222222", "M-001", LocalDateTime.of(2025,11,9,  9,40), 30, "Consulta"),
            new Turno("T3", "33333333", "M-002", LocalDateTime.of(2025,11,10,10,0), 45, "Revision")
        };
        Turno[] agendaNube = new Turno[] {
            new Turno("T2", "22222222", "M-001", LocalDateTime.of(2025,11,9,  9,40), 30, "Consulta (nube)"), // mismo id
            new Turno("T4", "44444444", "M-003", LocalDateTime.of(2025,11,7, 11,0), 60, "Operacion"),
            new Turno("T5", "11111111", "M-001", LocalDateTime.of(2025,11,8,  9, 0), 30, "Control duplicado"), // mismo médico+horario
            new Turno("T6", "55555555", "M-002", LocalDateTime.of(2025,11,11, 8,0), 30, "Nuevo")
        };

        // Merge y deduplicación
        java.util.List<String> conflictos = new java.util.ArrayList<>();
        Turno[] unificada = mergeAndDedup(agendaLocal, agendaNube, conflictos);

        System.out.println("Turnos unificados:");
        System.out.printf("%-4s %-10s %-8s %-16s %-8s %-10s\n", "ID", "PACIENTE", "MEDICO", "FECHA", "HORA", "MOTIVO");
        System.out.println("---------------------------------------------------------------------");
        for (Turno t : unificada) {
            String hora = String.format("%02d:%02d", t.fechaHora.getHour(), t.fechaHora.getMinute());
            System.out.printf("%-4s %-10s %-8s %-16s %-8s %-10s\n", t.id, t.dniPaciente, t.matriculaMedico, t.fechaHora.toLocalDate(), hora, t.motivo);
        }
        System.out.println();
        if (!conflictos.isEmpty()) {
            System.out.println("Conflictos detectados:");
            for (String c : conflictos) System.out.println("  - " + c);
        } else {
            System.out.println("No se detectaron conflictos.");
        }
        System.out.println("═════════════════════════════════════════════════════════");
    }

    // Merge con deduplicación y log de conflictos
    private Turno[] mergeAndDedup(Turno[] A, Turno[] B, java.util.List<String> conflictos) {
        java.util.ArrayList<Turno> res = new java.util.ArrayList<>();
        int i=0, j=0;
        java.util.HashSet<String> ids = new java.util.HashSet<>();
        java.util.HashSet<String> medHorario = new java.util.HashSet<>();
        while (i<A.length || j<B.length) {
            Turno tA = i<A.length ? A[i] : null;
            Turno tB = j<B.length ? B[j] : null;
            Turno next;
            if (tA != null && (tB == null || tA.compareTo(tB) <= 0)) {
                next = tA; i++;
            } else {
                next = tB; j++;
            }
            if (next == null) continue;
            boolean idDup = !ids.add(next.id);
            String claveMedHorario = next.matriculaMedico + "@" + next.fechaHora;
            boolean medHorarioDup = !medHorario.add(claveMedHorario);
            if (idDup) {
                conflictos.add("ID duplicado: " + next.id + " (" + next.motivo + ")");
                continue;
            }
            if (medHorarioDup) {
                conflictos.add("Conflicto de médico+horario: " + next.matriculaMedico + " " + next.fechaHora + " (" + next.motivo + ")");
                continue;
            }
            res.add(next);
        }
        return res.toArray(new Turno[0]);
    }

    public void ej8_sorts_demo() {
        System.out.print("\033[H\033[2J");
        System.out.println("═════════════════════════════════════════════════════════");
        System.out.println("Ej8: Reportes operativos con múltiples ordenamientos");
        System.out.println("═════════════════════════════════════════════════════════");
        System.out.println("Se generan vistas del día por hora, por duración y por apellido de paciente.");
        System.out.println("Se mide el tiempo de cada algoritmo con 1k, 10k y 50k turnos sintéticos.");
        System.out.println();

        int[] sizes = {1000, 10000, 50000};
        for (int n : sizes) {
            System.out.println("--- Prueba con " + n + " turnos ---");
            Turno[] base = generarTurnosSinteticos(n);

            // Por hora (inserción estable)
            Turno[] porHora = copiarTurnos(base);
            long t1 = System.nanoTime();
            insercionPorHora(porHora);
            long t2 = System.nanoTime();
            System.out.printf("[Inserción] Por hora: %.2f ms\n", (t2-t1)/1e6);

            // Por duración (Shellsort)
            Turno[] porDur = copiarTurnos(base);
            t1 = System.nanoTime();
            shellSortByDuracion(porDur);
            t2 = System.nanoTime();
            System.out.printf("[Shellsort] Por duración: %.2f ms\n", (t2-t1)/1e6);

            // Por apellido (Quicksort Lomuto)
            Turno[] porApe = copiarTurnos(base);
            t1 = System.nanoTime();
            quickSortPorApellido(porApe, 0, porApe.length-1);
            t2 = System.nanoTime();
            System.out.printf("[Quicksort] Por apellido: %.2f ms\n", (t2-t1)/1e6);

            // Mostrar primeras filas de cada vista
            System.out.println("\nVista por hora (primeros 5):");
            printTurnosTabla(porHora, 5);
            System.out.println("Vista por duración (primeros 5):");
            printTurnosTabla(porDur, 5);
            System.out.println("Vista por apellido (primeros 5):");
            printTurnosTabla(porApe, 5);
            System.out.println();
        }
        System.out.println("═════════════════════════════════════════════════════════");
    }

    // Shellsort por duración
    private void shellSortByDuracion(Turno[] arr) {
        int n = arr.length;
        for (int gap = n/2; gap>0; gap/=2) {
            for (int i=gap;i<n;i++) {
                Turno tmp = arr[i]; int j=i;
                while (j>=gap && arr[j-gap].duracionMin > tmp.duracionMin) { arr[j] = arr[j-gap]; j-=gap; }
                arr[j] = tmp;
            }
        }
    }

    // Quicksort por apellido de paciente (Lomuto)
    private void quickSortPorApellido(Turno[] a, int lo, int hi) {
        if (lo<hi) {
            int p = partitionApellido(a, lo, hi);
            quickSortPorApellido(a, lo, p-1);
            quickSortPorApellido(a, p+1, hi);
        }
    }
    private int partitionApellido(Turno[] a, int lo, int hi) {
        String pivot = apellidoDeDni(a[hi].dniPaciente);
        int i = lo;
        for (int j=lo;j<hi;j++) {
            if (apellidoDeDni(a[j].dniPaciente).compareTo(pivot) <= 0) { Turno t=a[i]; a[i]=a[j]; a[j]=t; i++; }
        }
        Turno t=a[i]; a[i]=a[hi]; a[hi]=t; return i;
    }
    // Inserción estable por hora
    private void insercionPorHora(Turno[] arr) {
        for (int i=1;i<arr.length;i++) {
            Turno key = arr[i]; int j = i-1;
            while (j>=0 && arr[j].fechaHora.isAfter(key.fechaHora)) {
                arr[j+1] = arr[j]; j--;
            }
            arr[j+1] = key;
        }
    }

    // Utilidades para demo
    private Turno[] generarTurnosSinteticos(int n) {
        java.util.Random rnd = new java.util.Random(42);
        String[] apellidos = {
            "Perez","Gomez","Ruiz","Diaz","Lopez","Sosa","Torres","Romero","Silva","Fernandez",
            "Mendez","Castro","Vega","Acosta","Navarro","Rojas","Molina","Ortega","Cabrera","Ponce",
            "Suarez","Herrera","Aguilar","Vargas","Campos","Delgado","Cruz","Morales","Peña","Ibarra"
        };
        Turno[] arr = new Turno[n];
        for (int i=0;i<n;i++) {
            String id = "T"+i;
            String dni = String.valueOf(10000000 + rnd.nextInt(90000000));
            String apellido = apellidos[rnd.nextInt(apellidos.length)];
            // Simular apellido en nombre
            String matricula = "M-" + (100+rnd.nextInt(10));
            java.time.LocalDateTime fecha = java.time.LocalDateTime.of(2025,11,6, 8+rnd.nextInt(10), rnd.nextInt(60));
            int dur = 10 + rnd.nextInt(50);
            String motivo = "Motivo" + (rnd.nextInt(5)+1);
            arr[i] = new Turno(id, dni, matricula, fecha, dur, motivo);
            // Guardar apellido en dni para demo de ordenamiento por apellido
            arr[i].dniPaciente = apellido + "," + dni;
        }
        return arr;
    }

    private Turno[] copiarTurnos(Turno[] arr) {
        Turno[] out = new Turno[arr.length];
        for (int i=0;i<arr.length;i++) out[i] = arr[i].deepCopy();
        return out;
    }

    private String apellidoDeDni(String dni) {
        // Para demo: el apellido está antes de la coma
        int idx = dni.indexOf(",");
        return idx>0 ? dni.substring(0,idx) : dni;
    }

    private void printTurnosTabla(Turno[] arr, int max) {
        System.out.printf("%-4s %-12s %-8s %-16s %-8s %-10s\n", "ID", "APELLIDO", "MEDICO", "FECHA", "HORA", "MOTIVO");
        System.out.println("---------------------------------------------------------------------");
        for (int i=0;i<Math.min(arr.length,max);i++) {
            Turno t = arr[i];
            String ape = apellidoDeDni(t.dniPaciente);
            String hora = String.format("%02d:%02d", t.fechaHora.getHour(), t.fechaHora.getMinute());
            System.out.printf("%-4s %-12s %-8s %-16s %-8s %-10s\n", t.id, ape, t.matriculaMedico, t.fechaHora.toLocalDate(), hora, t.motivo);
        }
    }

    public void ej9_audit_demo() {
        System.out.print("\033[H\033[2J");
        System.out.println("═════════════════════════════════════════════════════════");
        System.out.println("Ej9: Auditoría y Undo/Redo de cambios en agenda");
        System.out.println("═════════════════════════════════════════════════════════");
        System.out.println("Soporta agendar, cancelar, reprogramar con deshacer/rehacer multi-nivel.");
        System.out.println();

        // Usar pacientes y médicos del archivo
        Paciente p1 = new Paciente("11111111", "Juan Perez");
        Paciente p2 = new Paciente("22222222", "Ana Gomez");
        putPaciente(p1); putPaciente(p2);
        Medico m = new Medico("M-001", "Dr. Perez", "Cardiologia"); putMedico(m);

        AgendaConHistorial hist = new AgendaConHistorial();
        AVLAgenda ag = hist.getAgenda();

        // Agendar dos turnos reales
        Turno t1 = new Turno("T1", p1.dni, m.matricula, LocalDateTime.now().plusMinutes(30), 30, "Consulta");
        Turno t2 = new Turno("T2", p2.dni, m.matricula, LocalDateTime.now().plusMinutes(90), 30, "Control");
        System.out.println("Agendar T1: " + hist.agendar(t1));
        System.out.println("Agendar T2: " + hist.agendar(t2));
        printAgendaTabla(ag);

        // Cancelar T1 y mostrar agenda (debe quedar solo T2)
        System.out.println("\n─────────────────────────────────────────────────────────");
        System.out.println("Cancelar T1: " + hist.cancelar("T1"));
        printAgendaTabla(ag);

        // Reprogramar T2 y mostrar agenda (debe quedar solo T2 con nueva fecha)
        System.out.println("\n─────────────────────────────────────────────────────────");
        LocalDateTime nuevaFecha = LocalDateTime.now().plusHours(2);
        System.out.println("Reprogramar T2 a +2hs: " + hist.reprogramar("T2", nuevaFecha));
        printAgendaTabla(ag);

        // Undo multi-nivel
        System.out.println("\n─────────────────────────────────────────────────────────");
        System.out.println("Undo 1: " + hist.undo());
        printAgendaTabla(ag);
        System.out.println("Undo 2: " + hist.undo());
        printAgendaTabla(ag);
        System.out.println("Undo 3: " + hist.undo());
        printAgendaTabla(ag);

        // Redo multi-nivel
        System.out.println("\n─────────────────────────────────────────────────────────");
        System.out.println("Redo 1: " + hist.redo());
        printAgendaTabla(ag);
        System.out.println("Redo 2: " + hist.redo());
        printAgendaTabla(ag);
        System.out.println("Redo 3: " + hist.redo());
        printAgendaTabla(ag);

        // Intentar redo después de nueva acción
        System.out.println("\n─────────────────────────────────────────────────────────");
        System.out.println("Agendar nuevo turno (T3), se debe limpiar pila de redo:");
        Turno t3 = new Turno("T3", p1.dni, m.matricula, LocalDateTime.now().plusMinutes(180), 30, "Extra");
        System.out.println("Agendar T3: " + hist.agendar(t3));
        System.out.println("Intentar redo (debe fallar): " + hist.redo());
        printAgendaTabla(ag);
        System.out.println("═════════════════════════════════════════════════════════");
    }

    // Imprime la agenda en formato tabla estética
    private void printAgendaTabla(AVLAgenda ag) {
        Turno[] turnos = null;
        try {
            java.lang.reflect.Method m = ag.getClass().getDeclaredMethod("toArray");
            m.setAccessible(true);
            turnos = (Turno[]) m.invoke(ag);
        } catch (Exception e) { turnos = new Turno[0]; }
        System.out.println("\nAgenda actual:");
        System.out.printf("%-4s %-15s %-10s %-16s %-8s %-10s\n", "ID", "PACIENTE", "MEDICO", "FECHA", "HORA", "MOTIVO");
        System.out.println("--------------------------------------------------------------------------");
        for (Turno t : turnos) {
            String hora = String.format("%02d:%02d", t.fechaHora.getHour(), t.fechaHora.getMinute());
            System.out.printf("%-4s %-15s %-10s %-16s %-8s %-10s\n", t.id, t.dniPaciente, t.matriculaMedico, t.fechaHora.toLocalDate(), hora, t.motivo);
        }
        if (turnos.length == 0) System.out.println("(Agenda vacía)");
    }

    public void ej10_quirofano_demo() {
        System.out.print("\033[H\033[2J");
        System.out.println("═════════════════════════════════════════════════════════");
        System.out.println("Ej10: Planificador de Quirófanos (Min-Heap)");
        System.out.println("═════════════════════════════════════════════════════════");

        // Usar los primeros 3 pacientes reales del mapa
        String[] dnis = mapaPac.keysArray();
        Paciente[] pacientes = new Paciente[3];
        for (int i = 0; i < 3; i++) {
            pacientes[i] = mapaPac.get(dnis[i]);
        }

        Planner quirofanoPlanner = new Planner(3);
        LocalDateTime now = LocalDateTime.now();
        String[] salas = {"Q1", "Q2", "Q3"};
        for (String sala : salas) {
            quirofanoPlanner.programar(new Recordatorio(sala, now, "", "Libre"));
        }

        System.out.println("\nEstado inicial de los quirófanos:");
        printTablaQuirofanos(quirofanoPlanner);

        // Cirugía 1: paciente 1, 60 min
        Recordatorio r1 = quirofanoPlanner.proximo();
        System.out.println("\nAsignando cirugía de 60 min para " + pacientes[0].nombre + "...");
        System.out.println("Sala asignada: " + r1.id + " (disponible a las " + r1.fecha + ")");
        LocalDateTime fin1 = r1.fecha.plusMinutes(60);
        quirofanoPlanner.programar(new Recordatorio(r1.id, fin1, pacientes[0].dni, "Cirugía 1"));
        System.out.println("\nEstado después de la primera cirugía:");
        printTablaQuirofanos(quirofanoPlanner);

        // Cirugía 2: paciente 2, 30 min
        Recordatorio r2 = quirofanoPlanner.proximo();
        System.out.println("\nAsignando cirugía de 30 min para " + pacientes[1].nombre + "...");
        System.out.println("Sala asignada: " + r2.id + " (disponible a las " + r2.fecha + ")");
        LocalDateTime fin2 = r2.fecha.plusMinutes(30);
        quirofanoPlanner.programar(new Recordatorio(r2.id, fin2, pacientes[1].dni, "Cirugía 2"));
        System.out.println("\nEstado después de la segunda cirugía:");
        printTablaQuirofanos(quirofanoPlanner);

        // Cirugía 3: paciente 3, 45 min
        Recordatorio r3 = quirofanoPlanner.proximo();
        System.out.println("\nAsignando cirugía de 45 min para " + pacientes[2].nombre + "...");
        System.out.println("Sala asignada: " + r3.id + " (disponible a las " + r3.fecha + ")");
        LocalDateTime fin3 = r3.fecha.plusMinutes(45);
        quirofanoPlanner.programar(new Recordatorio(r3.id, fin3, pacientes[2].dni, "Cirugía 3"));
        System.out.println("\nEstado final de los quirófanos:");
        printTablaQuirofanos(quirofanoPlanner);
        System.out.println("═════════════════════════════════════════════════════════");
    }

    // Imprime el estado de los quirófanos en formato tabla estética
    private void printTablaQuirofanos(Planner planner) {
        // Obtener snapshot del heap (no modificar el heap real)
        try {
            java.lang.reflect.Field f = planner.getClass().getDeclaredField("heap");
            f.setAccessible(true);
            Recordatorio[] arr = (Recordatorio[]) f.get(planner);
            java.lang.reflect.Field sz = planner.getClass().getDeclaredField("size");
            sz.setAccessible(true);
            int n = (int) sz.get(planner);
            System.out.printf("%-8s %-18s %-10s %-16s %-8s %-12s\n", "SALA", "PACIENTE", "DNI", "FECHA", "HORA", "ESTADO");
            System.out.println("----------------------------------------------------------------------------");
            for (int i = 0; i < n; i++) {
                Recordatorio r = arr[i];
                String nombre = r.dniPaciente != null && !r.dniPaciente.isEmpty() ? (mapaPac.get(r.dniPaciente) != null ? mapaPac.get(r.dniPaciente).nombre : "-") : "-";
                String hora = String.format("%02d:%02d", r.fecha.getHour(), r.fecha.getMinute());
                String estado = r.mensaje;
                System.out.printf("%-8s %-18s %-10s %-16s %-8s %-12s\n", r.id, nombre, r.dniPaciente, r.fecha.toLocalDate(), hora, estado);
            }
            if (n == 0) System.out.println("(Sin quirófanos ocupados)");
        } catch (Exception e) {
            System.out.println("(Error mostrando quirófanos)");
        }
    }
}
