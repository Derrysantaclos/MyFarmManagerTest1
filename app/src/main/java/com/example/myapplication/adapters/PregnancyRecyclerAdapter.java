package com.example.myapplication.adapters;

import static com.example.myapplication.R.color.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import com.example.myapplication.R.color;
import com.example.myapplication.data.PregnancyDbHandler2;
import com.example.myapplication.models.Pregnancy;
import com.example.myapplication.ui.RabbitListDisplayPage;
import com.example.myapplication.util.DeleteDialog;
import com.example.myapplication.util.PregnancyDeleteDialog;
import com.example.myapplication.util.PregnancyFormDialog;

import java.util.ArrayList;

public class PregnancyRecyclerAdapter extends RecyclerView.Adapter<PregnancyRecyclerAdapter.ViewHolder>
{

    private final PregnancyDbHandler2 pregnancyDbHandler;
    private Context context;
    private final ArrayList<Pregnancy> pregnancyArrayList;
    private int filteredArraySize;

    public PregnancyRecyclerAdapter(Context context,ArrayList<Pregnancy> pregnancyArrayList, int filteredArraySize, PregnancyDbHandler2 pregnancyDbHandler)
    {
        this.pregnancyDbHandler= pregnancyDbHandler;
        this.context=context;
        this.pregnancyArrayList=pregnancyArrayList;
        this.filteredArraySize =filteredArraySize;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View pregnancyRecyclerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pregnancy_recycler,parent,false);
        return new ViewHolder(pregnancyRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        //pregnancyArrayList =pregnancyDbHandler.pregnancyArrayList();
        Pregnancy aPregnancy =pregnancyArrayList.get(position);
        holder.pregnancyRecyclerBuckTag.setText(aPregnancy.getBuckTag());
        holder.pregnancyRecyclerCrossedDate.setText(aPregnancy.getCrossedDate().toString());

        //holder.pregnancyRecyclerId.setText(aPregnancy.getId());
        holder.pregnancyRecyclerId.setText(String.valueOf(aPregnancy.getDoePregnancyCount()));
        holder.pregnancyRecyclerDoeTag.setText(aPregnancy.getDoeTag());
        holder.pregnancyConfirmationStatus.setText(aPregnancy.getPregnancyConfirmation());
//        if (holder.pregnancyConfirmationStatus.getText().toString().equalsIgnoreCase("True")){
//            holder.pregnancyConfirmationStatus.setTextColor(black);
//    }
        holder.pregnancyRecyclerNumberOfDays.setText( String.valueOf(aPregnancy.calculateNoOfDays()));
        Log.d("Preg", String.valueOf(aPregnancy.getDoePregnancyCount()));
    }


    @Override
    public int getItemCount() {
        return pregnancyArrayList.size();

    }

    //::::::::::::VIEW HOLDER CLASS::::::::::
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        public TextView pregnancyRecyclerBuckTag;
       public TextView pregnancyRecyclerDoeTag;
       public TextView pregnancyRecyclerCrossedDate;
      public TextView pregnancyRecyclerNumberOfDays;
      public TextView pregnancyRecyclerId;
      public Button pregnancyRecyclerDeleteButton;
        public Button pregnancyRecyclerEditButton;
        private Pregnancy selectedPregnancy;
        public TextView pregnancyConfirmationStatus;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pregnancyRecyclerBuckTag=itemView.findViewById(R.id.pregnancyRecyclerBuckTag);
            pregnancyRecyclerDoeTag=itemView.findViewById(R.id.pregnancyRecyclerDoeTag);
            pregnancyRecyclerCrossedDate=itemView.findViewById(R.id.pregnancyRecyclerCrossedDate);
            pregnancyRecyclerNumberOfDays=itemView.findViewById(R.id.pregnancyRecyclerNumberOfDays);
            pregnancyRecyclerId=itemView.findViewById(R.id.pregnancyRecyclerId);
            pregnancyRecyclerDeleteButton=itemView.findViewById(R.id.pregnanyRecyclerDeleteButton);
            pregnancyRecyclerEditButton=itemView.findViewById(R.id.pregnancyRecyclerEditButton);
            pregnancyConfirmationStatus=itemView.findViewById(R.id.pregnancyRecyclerConfirmationStatus);
            itemView.setOnClickListener(this);
            pregnancyRecyclerEditButton.setOnClickListener(this);
            pregnancyRecyclerDeleteButton.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
           selectedPregnancy = pregnancyArrayList.get(adapterPosition);

            switch (v.getId()){
                case R.id.pregnanyRecyclerDeleteButton:
                    selectedPregnancy = pregnancyArrayList.get(adapterPosition);
                    PregnancyDeleteDialog pregnancyDeleteDialog =new PregnancyDeleteDialog(context);
                    pregnancyDeleteDialog.showDeleteDialog();
                    pregnancyDeleteDialog.cancelDeleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pregnancyDeleteDialog.deleteDialog.dismiss();
                        }
                    });
                    pregnancyDeleteDialog.confirmDeleteButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {

                            pregnancyDeleteDialog.deleteDialog.dismiss();
                            deleteItem(selectedPregnancy.getId());


                            Toast.makeText(context,"Deleted", Toast.LENGTH_LONG ).show();
                        }
                    });
                    break;
                case R.id.pregnancyRecyclerEditButton:
                    PregnancyFormDialog pregnancyFormDialog = new PregnancyFormDialog(pregnancyDbHandler,context,selectedPregnancy.getDoeTag());
                    pregnancyFormDialog.editPregnancy(selectedPregnancy,PregnancyRecyclerAdapter.this,adapterPosition);
                    break;

            }


        }
        private void deleteItem(int pregnancyId){
            pregnancyDbHandler.deletePregnancy(pregnancyId);

            pregnancyArrayList.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
        }
    }
}
