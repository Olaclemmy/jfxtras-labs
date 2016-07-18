package jfxtras.labs.icalendaragenda.scene.control.agenda.behaviors;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;

import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.EditChoiceDialog;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.EditVComponentScene;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.EditVEventScene;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.revisors.ReviserVEvent;
import jfxtras.labs.icalendarfx.components.revisors.SimpleRevisorFactory;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities.DateTimeType;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class VEventBehavior extends DisplayableBehavior<VEvent>
{
//    public VEventBehavior()
//    {
//        super(agenda);
//    }

    @Override
    public EditVComponentScene getEditPopupScene(ICalendarAgenda agenda, Appointment appointment)
    {
        VEvent vComponent = (VEvent) agenda.appointmentVComponentMap().get(System.identityHashCode(appointment));
        if (vComponent == null)
        {
            // NOTE: Can't throw exception here because in Agenda there is a mouse event that isn't consumed.
            // Throwing an exception will leave the mouse unresponsive.
            System.out.println("ERROR: no component found - popup can'b be displayed");
            return null;
        } else
        {
            return new EditVEventScene(
                    vComponent,
                    agenda.getVCalendar().getVEvents(),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    agenda.getCategories());
        }
    }

    // TODO - TRY TO MOVE MOST OF BELOW METHOD TO SUPER
    @Override
    public void callRevisor(ICalendarAgenda agenda, Appointment appointment)
    {
        VEvent vComponent = (VEvent) agenda.appointmentVComponentMap().get(System.identityHashCode(appointment));
        VEvent vComponentOriginal = new VEvent(vComponent);
        if (vComponent == null)
        {
            // NOTE: Can't throw exception here because in Agenda there is a mouse event that isn't consumed.
            // Throwing an exception will leave the mouse unresponsive.
            System.out.println("ERROR: no component found - popup can'b be displayed");
        } else
        {
            Temporal startOriginalRecurrence = agenda.appointmentStartOriginalMap().get(System.identityHashCode(appointment));
            final Temporal startRecurrence;
            final Temporal endRecurrence;

            boolean wasDateType = DateTimeType.of(startOriginalRecurrence).equals(DateTimeType.DATE);
            boolean isNotDateType = ! DateTimeType.of(appointment.getStartTemporal()).equals(DateTimeType.DATE);
            boolean isChangedToTimeBased = wasDateType && isNotDateType;
            boolean isChangedToWholeDay = appointment.isWholeDay() && isNotDateType;
            if (isChangedToTimeBased)
            {
                startRecurrence = DateTimeType.DATE_WITH_LOCAL_TIME_AND_TIME_ZONE.from(appointment.getStartTemporal(), ZoneId.systemDefault());
                endRecurrence = DateTimeType.DATE_WITH_LOCAL_TIME_AND_TIME_ZONE.from(appointment.getEndTemporal(), ZoneId.systemDefault());
            } else if (isChangedToWholeDay)
            {
                startRecurrence = LocalDate.from(appointment.getStartTemporal());
                Temporal endInstanceTemp = LocalDate.from(appointment.getEndTemporal());
                endRecurrence = (endInstanceTemp.equals(startRecurrence)) ? endInstanceTemp.plus(1, ChronoUnit.DAYS) : endInstanceTemp; // make period between start and end at least one day
            } else
            {
                startRecurrence = appointment.getStartTemporal();
                endRecurrence = appointment.getEndTemporal();            
            }
            
            ReviserVEvent newRevisor = (ReviserVEvent) SimpleRevisorFactory.newReviser(vComponent);
            newRevisor.withDialogCallback(EditChoiceDialog.EDIT_DIALOG_CALLBACK)
                    .withEndRecurrence(endRecurrence)
                    .withStartOriginalRecurrence(startOriginalRecurrence)
                    .withStartRecurrence(startRecurrence)
                    .withVComponentEdited(vComponent)
                    .withVComponentOriginal(vComponentOriginal);
            Collection<VEvent> newVComponents = newRevisor.revise();
            agenda.getVCalendar().getVEvents().remove(vComponent);
            if (newVComponents != null)
            {
                agenda.getVCalendar().getVEvents().addAll(newVComponents);
            }
        }        
    }
    
//    @Override
//    public void callDeleter(VComponent vComponent, List<VComponent> vComponents, Temporal startOriginalRecurrence)//ICalendarAgenda agenda, Appointment appointment)
//    {
////        VEvent vComponent = (VEvent) agenda.appointmentVComponentMap().get(System.identityHashCode(appointment));
//        VEvent vComponentNew = new DeleterVEvent((VEvent) vComponent)
//                .withDialogCallback(DeleteChoiceDialog.DELETE_DIALOG_CALLBACK)
//                .withStartOriginalRecurrence(startOriginalRecurrence)
//                .delete();
//        vComponents.remove(vComponent);
//        if (vComponentNew != null)
//        {
//            vComponents.add(vComponentNew);
//        }
//    }

    @Override
    public void callDeleter(VComponent vComponent, List<? extends VComponent> vComponents,
            Temporal startOriginalRecurrence)
    {
        // TODO Auto-generated method stub
        
    }
}
