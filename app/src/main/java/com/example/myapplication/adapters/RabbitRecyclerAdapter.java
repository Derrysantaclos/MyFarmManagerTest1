package com.example.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.DbHandler;
import com.example.myapplication.models.Rabbit;
import com.example.myapplication.ui.RabbitFullDetails;
import com.example.myapplication.util.DeleteDialog;
import com.example.myapplication.util.RabbitFormDialog;

import java.util.ArrayList;

public class RabbitRecyclerAdapter extends RecyclerView.Adapter<RabbitRecyclerAdapter.ViewHolder> {
    private View rabbitRecyclerView;
    private Context context;//needed since we would be using it on another page
    private ArrayList<Rabbit> rabbitArrayList;
    private DbHandler dbHandler;



    public RabbitRecyclerAdapter(Context context, ArrayList<Rabbit> rabbitArrayList, DbHandler dbHandler) {
        this.dbHandler =dbHandler;
        this.context = context;
        this.rabbitArrayList = rabbitArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rabbitRecyclerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rabbit_recycler, parent,false);

        return new ViewHolder(rabbitRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rabbit rabbit = rabbitArrayList.get(position);
        holder.rabbitAge.setText(rabbit.get_age());
        holder.rabbitBreed.setText(rabbit.get_breed());
        holder.rabbitTag.setText(rabbit.get_tag());
    }

    @Override
    public int getItemCount() {
        return rabbitArrayList.size();
    }

    //just to filter the list based on search
    public void filterArrayList(ArrayList<Rabbit> filteredArrayList){
        rabbitArrayList=filteredArrayList;
        notifyDataSetChanged();
    }


    //::::::::::::::::::::::::VIEW HOLDER CLASS:::::::::::::::::::::::::::::::::::::::::::
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView rabbitTag;
        public TextView rabbitBreed;
        public TextView rabbitAge;
        public Button recyclerEditButton;
        private Button recyclerDeleteButton;
        private RabbitFormDialog rabbitFormDialog;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //where the items in the view will be held together
            //the view was gotten up
            rabbitTag = itemView.findViewById(R.id.rabbitRecyclerTag);
            rabbitBreed = itemView.findViewById(R.id.rabbitRecyclerBreed);
              rabbitAge = itemView.findViewById(R.id.rabbitRecyclerAge);
              recyclerDeleteButton =itemView.findViewById(R.id.recyclerDeleteButton);
              recyclerEditButton =itemView.findViewById(R.id.recyclerEditButton);
              //set on clicklistener for all clickables
              recyclerEditButton.setOnClickListener(this);
              recyclerDeleteButton.setOnClickListener(this);
              itemView.setOnClickListener(this);//the whole item

        }

        @Override
        public void onClick(View v) {
            //get the current rabbit in the chosen view
           Rabbit currentRabbit = rabbitArrayList.get(getAdapterPosition());

            rabbitFormDialog= new RabbitFormDialog(context,dbHandler);

            switch(v.getId()){
                case R.id.recyclerEditButton:
                    //pop up form and update
                    rabbitFormDialog.showCurrentRabbitDialog(currentRabbit);

                    break;
                case R.id.recyclerDeleteButton:
                    DeleteDialog deleteDialog =new DeleteDialog(context,dbHandler);
                    deleteDialog.showDeleteDialog(currentRabbit.get_id());
                    break;
                default:
                    //rabbit details
                    //goes to the fuul detail of the rabbit
                    Intent rabbitDetailIntent = new Intent(context, RabbitFullDetails.class);
                    rabbitDetailIntent.putExtra("adapterPosition", getAdapterPosition());
                    rabbitDetailIntent.putExtra("currentRabbitTag", currentRabbit.get_tag());
                    rabbitDetailIntent.putExtra("currentRabbitBreed", currentRabbit.get_breed());
                    rabbitDetailIntent.putExtra("currentRabbitColour", currentRabbit.get_colour());
                    rabbitDetailIntent.putExtra("currentRabbitAge", currentRabbit.get_age());
                    rabbitDetailIntent.putExtra("currentRabbitDateOfBirth", currentRabbit.get_dateOfBirth());
                    rabbitDetailIntent.putExtra("currentRabbitSource", currentRabbit.get_source());
                    rabbitDetailIntent.putExtra("currentRabbitSex", currentRabbit.get_sex());

                    context.startActivity(rabbitDetailIntent);
                    Toast.makeText(context,currentRabbit.get_tag(),Toast.LENGTH_LONG).show();
                    break;
            }

        }
    }

}
