package edu.hm.cs.bikebattle.app.fragments.navigationdrawer;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.activities.BaseActivity;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.modell.User;

/**
 * Shows a new Fragment with the user informations. In this fragment the user
 * has a possibility to change his informations.
 * @author  sven schulz
 */
public class ProfilFragment extends Fragment implements View.OnClickListener{
  /** Tag from the current Fragment.*/
  private static final String TAG = "ProfilFragment";
  /** Current user.*/
  private User user;
  /**Uri includes user picture.*/
  private String uri;
  /**Button for set the user weight.*/
  private ImageButton editWeight;
  /**Button for set the user size.*/
  private ImageButton editSize;
  /**Label  user size.*/
  private TextView sizeView;
  /**Label  user weight.*/
  private TextView weightView;
  /**Current Layout*/
  private SlidingUpPanelLayout mSlidingUpPanelLayout;
  /**Boolean for editable.*/
  private boolean editable;

  /**
   * This method creates a new Fragment,with the required Informations
    * @param user - is the current User from the App.
   * @param uri - is the data link to the user profil picture.
   * @return new Fragment
   */
  public static ProfilFragment newInstance(User user, String uri, boolean editable) {
    ProfilFragment fragment = new ProfilFragment();
    fragment.user = user;
    fragment.uri = uri;
    fragment.editable = editable;

    return  fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    getActivity().setTitle(getString(R.string.Menu_Profil));

    View view = inflater.inflate(R.layout.fragment_profil, container, false);
    setupButtons(view);

    mSlidingUpPanelLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);

    TextView adressView = (TextView)view.findViewById(R.id.youradressfieldprofil);
    adressView.setText(user.getEmail());

    TextView nameView = (TextView)view.findViewById(R.id.name_text);
    nameView.setText(user.getName());

    weightView = (TextView)view.findViewById(R.id.weight_text);
    float weight = user.getWeightInKg();
    if(weight == 0.0f){
      weight = 00.00f;
    }
    weightView.setText(Float.toString(weight));

    sizeView = (TextView)view.findViewById(R.id.size_text);
    float size = user.getSizeInMeter();
    if(size == 0.0f){
      size = 00.00f;
    }
    sizeView.setText(Float.toString(size));

    ImageView profilView = (ImageView)view.findViewById(R.id.imageViewprofil);
    if (uri != null) {
      Picasso
          .with(view.getContext())
          .load(uri)
          .fit()
          .centerCrop()
          .placeholder(R.mipmap.ic_launcher)
          .error(R.mipmap.ic_launcher)
          .into(profilView);
    }

    return view;
  }

  /**
   * initialize buttons.
   * @param view - current view.
   */
  public void setupButtons(View view){

    editSize = (ImageButton) view.findViewById(R.id.size_edit);
    editWeight = (ImageButton) view.findViewById(R.id.weight_edit);

    if(editable) {
      editSize.setOnClickListener(this);
      editWeight.setOnClickListener(this);
    } else {
      editSize.setVisibility(View.GONE);
      editWeight.setVisibility(View.GONE);
    }
  }

  @Override
  public void onClick(View v) {

    switch (v.getId()){
      case R.id.size_edit:
        setupDialog(v,"Bitte geben Sie ihre Größe ein.",true,sizeView).show();
        break;
      case R.id.weight_edit:
        setupDialog(v,"Bitte geben Sie ihr Gewicht ein.",false,weightView).show();
        break;
      default:
        break;
    }
  }

  /**
   * This method set a Dialog with different informations for the user, where he can typ some
   * informations into.
   * @param text - different information.
   * @param size - true if the user want to change his size, false then the user want to change his
   *             weight.
   * @param textView - textview.
   * @return  Dialog Window on the screem-
   */
  public Dialog setupDialog(final View v, String text, final boolean size, final TextView textView){
    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
    dialog.setMessage(text);

    final EditText input = new EditText(v.getContext());
    input.setId(0);
    input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    dialog.setView(input);

    dialog.setPositiveButton("Ändern", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if(input.getText().toString().length() > 0){
          float value = Float.parseFloat(input.getText().toString());
          if(size){
            user.setSizeInMeter(value);
            textView.setText(value+"");
          }
          else{
            user.setWeightInKg(value);
            textView.setText(value+"");
          }
          //Updates the User in the backend
          BaseActivity activity = (BaseActivity)getActivity();
          activity.getDataConnector().changeUserData(user, new Consumer<Void>() {
            @Override
            public void consume(Void input) {}
            @Override
            public void error(int error, Throwable exception) {
              Log.e(TAG, "UPDATE USER FAILURE: " + exception.getMessage());
            }
          });

        }
        else {
          Toast toast = Toast.makeText(v.getContext(),"Fehlerhafte Eingabe",Toast.LENGTH_SHORT);
          toast.show();
        }
        input.setText("");
        return;
      }
    });

    dialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        return;
      }
    });

    return dialog.create();
  }
}
