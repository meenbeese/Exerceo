package com.health.openworkout.gui.training;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.health.openworkout.R;
import com.health.openworkout.core.datatypes.GitHubFile;
import com.health.openworkout.core.datatypes.TrainingPlan;
import com.health.openworkout.core.utils.PackageUtils;
import com.health.openworkout.gui.datatypes.GenericAdapter;

import java.io.File;
import java.util.List;

public class TrainingsDatabaseFragment extends Fragment {
    private RecyclerView trainingsView;
    private ProgressBar progressBar;
    private Button buttonView;

    private List<GitHubFile> gitHubFileList;
    private TrainingDatabaseAdapter trainingDatabaseAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_trainingdatabase, container, false);

        trainingsView = root.findViewById(R.id.trainingsView);
        progressBar = root.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);

        trainingsView.setHasFixedSize(true);
        trainingsView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadFromDatabase();

        return root;
    }

    protected void loadFromDatabase() {
        PackageUtils packageUtils = new PackageUtils(getContext());

        packageUtils.setOnGitHubCallbackListener(new PackageUtils.OnGitHubCallbackListener() {
            @Override
            public void onGitHubFileList(List<GitHubFile> receivedGitHubFileList) {
                gitHubFileList = receivedGitHubFileList;
                trainingDatabaseAdapter = new TrainingDatabaseAdapter(getContext(), gitHubFileList);

                trainingDatabaseAdapter.setOnItemClickListener(new GenericAdapter.OnGenericClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        GitHubFile gitHubFile = gitHubFileList.get(position);
                        packageUtils.downloadFile(gitHubFile);
                        trainingDatabaseAdapter.notifyItemChanged(position);
                    }
                });

                trainingsView.setAdapter(trainingDatabaseAdapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onGitHubDownloadFile(File filename) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TrainingPlan trainingPlan = packageUtils.importTrainingPlan(filename);

                        trainingDatabaseAdapter.downloadCompleted(trainingPlan);
                    }
                });
            }

            @Override
            public void onGitHubDownloadProgressUpdate(long bytesDownloaded, long bytesTotal) {
                trainingDatabaseAdapter.updateProgressBar(bytesDownloaded, bytesTotal);
            }

            @Override
            public void onGitHubFailure(Exception ex) {
                Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });

        packageUtils.getGitHubFiles();
    }
}
