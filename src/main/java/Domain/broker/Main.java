package Domain.broker;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;


public class Main {
    public static void main(String[] args) {
        init();
        startScheduler();
    }

    private static void init() {
        // Inicializar lo que necesites aquí
        System.out.println("Inicialización completada.");
    }

    // Método para iniciar el scheduler de Quartz
    private static void startScheduler() {
        try {
            // Crear el scheduler con el SchedulerFactory
            SchedulerFactory sf = new StdSchedulerFactory();
            Scheduler scheduler = sf.getScheduler();

            // Definir el Job y enlazarlo a nuestra clase (en este caso CroneTask_Inventario)
            JobDetail job1 = JobBuilder.newJob(CroneTask_Inventario.class)
                    .withIdentity("inventoryJob", "grupo1")
                    .build();

            // Crear un Trigger/Disparador para que el Job se ejecute cada 30 segundos
            Trigger trigger1 = TriggerBuilder.newTrigger()
                    .withIdentity("inventoryTrigger", "grupo1")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(30) // Cambiar a cada 30 segundos para prueba
                            .repeatForever())
                    .build();

            // Asignar el job al scheduler
            scheduler.scheduleJob(job1, trigger1);

            // Iniciar el scheduler
            scheduler.start();

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}


