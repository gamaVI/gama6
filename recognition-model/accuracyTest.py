import cv2
import csv
import pandas as pd
from collections import Counter

import os

import pandas as pd

#_______________________________________________________
def process_images_and_write_results(image_folder):
    with open(".\\recognition-model\\static-data.csv", 'w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(['Image', 'TotalDetected'])

        # Iterate over each image in the directory
        for image_name in os.listdir(image_folder):
            image_path = os.path.join(image_folder, image_name)
            if os.path.isfile(image_path):
                try:
                    # Reset the list for each image
                    detected_classNames.clear()
                    # Use your model to process the image
                    from_static_image(image_path)
                    # Count the frequency of detected classes
                    frequency = Counter(detected_classNames)
                    # Calculate the total number of vehicles detected
                    total_vehicles_detected = sum(frequency.values())
                    # Write the results to static-data.csv
                    writer.writerow([image_name, total_vehicles_detected])
                except Exception as e:
                    print(f"Error processing {image_name}: {e}")


def compare_results_with_expected(csv_filename):
    # Read the expected results, specifying the delimiter and handling NaN values
    expected_df = pd.read_csv(csv_filename, delimiter=';', skip_blank_lines=True)
    expected_df.dropna(subset=['Name'], inplace=True)
    expected_df['Name'] = expected_df['Name'].astype(str)
    expected_df['Name'] = expected_df['Name'].apply(lambda x: os.path.basename(x) if pd.notnull(x) else x)

    # Read the results from your model
    results_df = pd.read_csv(".\\recognition-model\\static-data.csv")
    results_df['Image'] = results_df['Image'].apply(lambda x: os.path.basename(x) if pd.notnull(x) else x)
    results_df.rename(columns={'Image': 'Name'}, inplace=True)

    # Merge the dataframes on the 'Name' column
    comparison_df = expected_df.merge(results_df, on='Name', how='left')
    comparison_df['TotalDetected'].fillna(0, inplace=True)
    comparison_df['numOfVehicle'] = comparison_df['numOfVehicle'].fillna(0).astype(int)

    # Calculate the differences between actual and predicted counts
    comparison_df['Difference'] = comparison_df['numOfVehicle'] - comparison_df['TotalDetected']

    # Calculate accuracy
    correct_predictions = sum(comparison_df['numOfVehicle'] == comparison_df['TotalDetected'])
    total_predictions = comparison_df['numOfVehicle'].notnull().sum()
    accuracy = correct_predictions / total_predictions if total_predictions else 0

    # Calculate average and standard deviation
    average_diff = comparison_df['Difference'].mean()
    std_deviation = comparison_df['Difference'].std()

    print(f"Model accuracy: {accuracy:.2%}")
    print(f"Average difference: {average_diff:.2f}") #nam pove za koliko se ponavadi zmoti model, če je negativno pomeni da ponavadi katerega spusti, če pozitivno da vidi avte tam kjer jih ni
    print(f"Standard deviation: {std_deviation:.2f}")

    #print(comparison_df)
    
    comparison_df.to_csv('.\\recognition-model\\comparison_output.csv', index=False)