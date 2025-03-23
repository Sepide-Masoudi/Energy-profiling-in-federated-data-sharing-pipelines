# Energy Profiling in Federated Data-Sharing Pipeline
This repository contains the code and resources for simulating and analyzing energy profiling in federated data-sharing pipelines. The project focuses on evaluating how the proposed method can be applied.
This program will first build a synthetic dataset of pipelines based on the sample dataset which defines the type of stages and resource required for each of them. In the next step, the simulator will find the common stages between different pipelines and generate a xlsx output file which shows the energy consumption. 

## Repository Structure

The repository is organized as follows:

- **`/src`**: Contains the source code for the simulation program.
- **`src/main/resources`**: Stores the configurations and the sample datasets used for defining type of stages and metrics related to them .


## Key Files

Here are the paths to two important files in this repository:

1. **Simulation Program**:  
   The main simulation program can be found at:  
   [`src/main/java/com/tub/ise/energyprofilersimulator/EnergyProfilerSimulatorApplication.java`](/src/EnergyProfilerSimulatorApplication.java)

2. **Sample Output .xlsx File**:  
   The two generated .xlsx files by simulator are located at:  
   [`model_output-69.xlsx`](/model_output-69.xlsx)
   [`pipeline_list_output-30.xlsx`](/pipeline_list_output-30.xlsx)
3. **Input .csv File**
    This file shows the types of stages(operations) and energy consumption of them, located at:
   [`src/main/resources/operations-definition.csv`](src/main/resources/operations-definition.csv)

## How to Use

1. Clone the repository:
   ```bash
   git clone https://github.com/Sepide-Masoudi/Energy-profiling-in-federated-data-sharing-pipelines.git
run the /src/EnergyProfilerSimulatorApplication.java class.