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
        System.out.println("Pacientes: " + mapaPac.size());
        System.out.println("Medicos: " + medCount);
        System.out.println("Turnos cargados: " + turnoCount);
    }

    public void ej2_agenda_demo() {
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
            System.out.printf("%-7s %-18s %-17s %-20s\n", "ID", "PACIENTE", "FECHA Y HORA", "MOTIVO");
            System.out.println("----------------------------------------------");
            Turno[] turnos = aToArray(a);
            boolean hayTurnos = false;
            for (Turno t : turnos) {
                if (!t.matriculaMedico.equals(m.matricula)) continue;
                Paciente p = mapaPac.get(t.dniPaciente);
                String nombrePac = p != null ? p.nombre : t.dniPaciente;
                String hora = String.format("%02d/%02d %02d:%02d hs", t.fechaHora.getDayOfMonth(), t.fechaHora.getMonthValue(), t.fechaHora.getHour(), t.fechaHora.getMinute());
                System.out.printf("%-7s %-18s %-17s %-20s\n", t.id, nombrePac, hora, t.motivo);
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
        System.out.println("Ej3 primerHueco:");
        Medico m = new Medico("M-003","Dra Hueco","Spec"); putMedico(m);
        int mi = findMedIndex("M-003");
        AVLAgenda a = agendas[mi];
        putPaciente(new Paciente("80000001","Hueco1"));
        LocalDateTime base = LocalDateTime.now().plusHours(2);
        a.agendar(new Turno("H1","80000001", m.matricula, base, 30, ""));
        a.agendar(new Turno("H2","80000001", m.matricula, base.plusMinutes(45), 30, ""));
        System.out.println("Primer hueco >= base-15: " + a.primerHueco(base.minusMinutes(15), 30));
    }

    public void ej4_salaEspera_demo() {
        System.out.println("Ej4 SalaEspera:");
        SalaEspera s = new SalaEspera(4);
        s.llega("111"); s.llega("222"); s.llega("333"); s.llega("444"); s.dump();
        s.llega("555"); s.dump();
        System.out.println("Atendiendo: " + s.atiende()); s.dump();
    }

    public void ej5_planner_demo() {
        System.out.println("Ej5 Planner:");
        Planner p = new Planner(8);
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        p.programar(new Recordatorio("R1", now.plusMinutes(5), "111", "rec1"));
        p.programar(new Recordatorio("R2", now.plusMinutes(2), "222", "rec2"));
        p.dump();
        System.out.println("Proximo: " + p.proximo());
        p.reprogramar("R1", now.plusMinutes(1));
        p.dump();
    }

    public void ej6_hashpacientes_demo() {
        System.out.println("Ej6 MapaPacientes:");
        putPaciente(new Paciente("70000001","Ana"));
        putPaciente(new Paciente("70000002","Luis"));
        System.out.println("Contains 70000001: " + mapaPac.containsKey("70000001"));
        System.out.println("Get 70000002: " + mapaPac.get("70000002"));
        String[] keys = mapaPac.keysArray();
        System.out.println("Keys:");
        for (String k : keys) System.out.println("  " + k);
    }

    public void ej7_merge_demo() {
        System.out.println("Ej7 Merge demo:");
        // simple array merge
        Turno[] A = new Turno[] { new Turno("A1","1","M1", LocalDateTime.now().plusHours(1), 30, "") };
        Turno[] B = new Turno[] { new Turno("B1","2","M1", LocalDateTime.now().plusHours(2), 30, "") };
        Turno[] C = mergeSorted(A,B);
        for (Turno t : C) System.out.println(" merged: " + t);
    }

    private Turno[] mergeSorted(Turno[] A, Turno[] B) {
        Turno[] out = new Turno[A.length + B.length];
        int i=0,j=0,k=0;
        while (i<A.length && j<B.length) {
            if (A[i].compareTo(B[j]) <= 0) out[k++]=A[i++]; else out[k++]=B[j++];
        }
        while (i<A.length) out[k++]=A[i++]; while (j<B.length) out[k++]=B[j++];
        Turno[] res = new Turno[k]; System.arraycopy(out,0,res,0,k); return res;
    }

    public void ej8_sorts_demo() {
        System.out.println("Ej8 Sorts:");
        Turno[] arr = new Turno[5];
        for (int i=0;i<5;i++) arr[i] = new Turno("T"+i, ""+(5-i), "M", LocalDateTime.now().plusMinutes(i*10), 10+i, "");
        // shell by duration
        shellSortByDuration(arr);
        for (Turno t : arr) System.out.println("  " + t);
        // quick by patient DNI
        quickSortByPaciente(arr, 0, arr.length-1);
        for (Turno t : arr) System.out.println("  " + t);
    }

    private void shellSortByDuration(Turno[] arr) {
        int n = arr.length;
        for (int gap = n/2; gap>0; gap/=2) {
            for (int i=gap;i<n;i++) {
                Turno tmp = arr[i]; int j=i;
                while (j>=gap && arr[j-gap].duracionMin > tmp.duracionMin) { arr[j] = arr[j-gap]; j-=gap; }
                arr[j] = tmp;
            }
        }
    }

    private void quickSortByPaciente(Turno[] a, int lo, int hi) {
        if (lo<hi) {
            int p = partition(a, lo, hi);
            quickSortByPaciente(a, lo, p-1);
            quickSortByPaciente(a, p+1, hi);
        }
    }
    private int partition(Turno[] a, int lo, int hi) {
        String pivot = a[hi].dniPaciente;
        int i = lo;
        for (int j=lo;j<hi;j++) {
            if (a[j].dniPaciente.compareTo(pivot) <= 0) { Turno t=a[i]; a[i]=a[j]; a[j]=t; i++; }
        }
        Turno t=a[i]; a[i]=a[hi]; a[hi]=t; return i;
    }

    public void ej9_audit_demo() {
        System.out.println("Ej9 Auditoría:");
        AgendaConHistorial hist = new AgendaConHistorial();
        AVLAgenda ag = hist.getAgenda();
        // demo create paciente + turno
        putPaciente(new Paciente("60000001","Undo1"));
        Medico m = new Medico("M-AUD","Dr Aud","Gen"); putMedico(m);
        Turno t = new Turno("AUD1","60000001", m.matricula, LocalDateTime.now().plusMinutes(30), 30, "");
        System.out.println("Agendar AUD1: " + hist.agendar(t));
        ag.printInOrder();
        System.out.println("Undo: " + hist.undo());
        ag.printInOrder();
        System.out.println("Redo: " + hist.redo());
        ag.printInOrder();
    }

    public void ej10_quirofano_demo() {
        System.out.println("Ej10 Planificador Quirofano:");
        // Use Planner to find the next available operating room (quirofano)
        Planner quirofanoPlanner = new Planner(3);
        LocalDateTime now = LocalDateTime.now();

        // Initial state: all rooms are available now.
        String[] ids = new String[] {"Q1","Q2","Q3"};
        for (String id : ids) {
            // Use a Recordatorio to track when a room is free.
            // The 'fecha' is the time the room becomes available.
            quirofanoPlanner.programar(new Recordatorio(id, now, "", "Libre"));
        }

        System.out.println("Estado inicial de los quirófanos:");
        quirofanoPlanner.dump();

        // Schedule a 60-minute surgery for patient "P-Q1"
        // 1. Get the next available room (the one that is free the earliest)
        Recordatorio proximaSalaLibre = quirofanoPlanner.proximo();
        System.out.println("\nAsignando cirugía de 60 min para P-Q1...");
        System.out.println("Sala asignada: " + proximaSalaLibre.id + " (disponible a las " + proximaSalaLibre.fecha + ")");

        // 2. The new surgery will occupy this room. The room will be free after the surgery.
        LocalDateTime finCirugia1 = proximaSalaLibre.fecha.plusMinutes(60);
        quirofanoPlanner.programar(new Recordatorio(proximaSalaLibre.id, finCirugia1, "P-Q1", "Cirugía 1"));

        System.out.println("Estado después de la primera cirugía:");
        quirofanoPlanner.dump();

        // Schedule a 30-minute surgery for patient "P-Q2"
        proximaSalaLibre = quirofanoPlanner.proximo();
        System.out.println("\nAsignando cirugía de 30 min para P-Q2...");
        System.out.println("Sala asignada: " + proximaSalaLibre.id + " (disponible a las " + proximaSalaLibre.fecha + ")");
        LocalDateTime finCirugia2 = proximaSalaLibre.fecha.plusMinutes(30);
        quirofanoPlanner.programar(new Recordatorio(proximaSalaLibre.id, finCirugia2, "P-Q2", "Cirugía 2"));

        System.out.println("Estado después de la segunda cirugía:");
        quirofanoPlanner.dump();

        // Schedule another 45-minute surgery for patient "P-Q3"
        proximaSalaLibre = quirofanoPlanner.proximo();
        System.out.println("\nAsignando cirugía de 45 min para P-Q3...");
        System.out.println("Sala asignada: " + proximaSalaLibre.id + " (disponible a las " + proximaSalaLibre.fecha + ")");
        LocalDateTime finCirugia3 = proximaSalaLibre.fecha.plusMinutes(45);
        quirofanoPlanner.programar(new Recordatorio(proximaSalaLibre.id, finCirugia3, "P-Q3", "Cirugía 3"));

        System.out.println("Estado final de los quirófanos:");
        quirofanoPlanner.dump();
    }

    public void runAllDemos() {
        ej1_validaciones(); ej2_agenda_demo(); ej3_primerHueco_demo(); ej4_salaEspera_demo();
        ej5_planner_demo(); ej6_hashpacientes_demo(); ej7_merge_demo(); ej8_sorts_demo();
        ej9_audit_demo(); ej10_quirofano_demo();
    }
}
