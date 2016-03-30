package edu.hm.cs.bikebattle.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by lukas on 26.03.2016.
 */
public class RouteInfoDialog extends DialogFragment {
    private final OSMRoute route;

    public RouteInfoDialog(OSMRoute route){
        super();
        this.route=route;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*
        String name=savedInstanceState.getString("name");
        double length=savedInstanceState.getDouble("length");
        */

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.routeinfodialog)
                .setIcon(R.drawable.ic_bike)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
