package layout;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tuturu.spark.mangareader.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileExplorerRow extends Fragment implements View.OnClickListener {

    ItemType itemType;
    String itemName;
    String path;
    Boolean isExpanded;
    ArrayList<FileExplorerRow> childFragments;

    // слушатель события
    private OnFragmentInteractionListener mListener;


    // ctor
    public FileExplorerRow() {
        childFragments = new ArrayList<>();
        isExpanded = false;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_explorer_row, container, false);

        ((TextView)view.findViewById(R.id.ItemType)).setText(itemType.toString());
        ((TextView)view.findViewById(R.id.ItemName)).setText(itemName);

        view.setClickable(true);
        view.setOnClickListener(this);

        return view;
    }

    // Здесь присобачиваем родителя как слушателя события
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {

        if (isExpanded)
            colapseFolder();
        else
            expandFolder();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }

    public enum ItemType{
        Folder,
        File
    }

    public void init(ItemType type, String name, String path){

        itemType = type;
        itemName = name;
        this.path = path;
    }

    private void expandFolder(){

        childFragments.clear();

        File dir = new File(path);

        File[] files = dir.listFiles();

        if (files == null)
            return;

        for (File file : files){

            FileExplorerRow row = new FileExplorerRow();

            if (file.isDirectory()){
                row.init(FileExplorerRow.ItemType.Folder, file.getName(), file.getPath());
            } else {
                row.init(FileExplorerRow.ItemType.File, file.getName(), file.getPath());
            }

            getChildFragmentManager().beginTransaction().add(R.id.expandContainer, row, file.getPath()).commit();

            childFragments.add(row);
        }

        isExpanded = true;
    }

    public void colapseFolder(){

        if (childFragments.size() == 0)
            return;

        for (FileExplorerRow frag: childFragments) {

            frag.colapseFolder();

            getChildFragmentManager().beginTransaction().remove(frag).commit();
        }

        childFragments.clear();

        isExpanded = false;
    }
}
