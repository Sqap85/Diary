import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class DateRangePicker extends JDialog {
    private final JSpinner startDateSpinner;
    private final JSpinner endDateSpinner;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean confirmed = false;

    public DateRangePicker(JFrame parent) {
        super(parent, "Select Date Range", true);
        setLayout(new GridLayout(3, 2));
        setSize(400, 150);
        setLocationRelativeTo(parent);

        add(new JLabel("Start Date:"));
        startDateSpinner = new JSpinner(new SpinnerDateModel());
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd"));
        add(startDateSpinner);

        add(new JLabel("End Date:"));
        endDateSpinner = new JSpinner(new SpinnerDateModel());
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd"));
        add(endDateSpinner);

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> {
            startDate = LocalDate.parse(new java.text.SimpleDateFormat("yyyy-MM-dd")
                    .format(startDateSpinner.getValue()));
            endDate = LocalDate.parse(new java.text.SimpleDateFormat("yyyy-MM-dd")
                    .format(endDateSpinner.getValue()));
            confirmed = true;
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        add(okButton);
        add(cancelButton);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}