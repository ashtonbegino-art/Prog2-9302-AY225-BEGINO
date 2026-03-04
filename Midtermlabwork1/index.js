const fs = require("fs");
const readline = require("readline");

// Create readline interface
const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

// Function to start program
function startProgram() {
    rl.question("Enter FULL dataset file path: ", (filePath) => {
        validateFile(filePath);
    });
}

// Function to validate file
function validateFile(filePath) {
    try {
        // Check if file exists and is readable
        if (!fs.existsSync(filePath)) {
            console.log("Error: File does not exist.\n");
            return startProgram();
        }

        const data = fs.readFileSync(filePath, "utf-8");

        // Basic CSV validation
        if (!data.includes(",")) {
            console.log("Error: File is not in valid CSV format.\n");
            return startProgram();
        }

        console.log("\nFile loaded successfully!\n");
        processData(data);

    } catch (error) {
        console.log("Error reading file.\n");
        startProgram();
    }
}

// Function to process dataset
function processData(data) {
    const lines = data.trim().split("\n");

    let productTotals = {};
    let grandTotal = 0;

    for (let i = 1; i < lines.length; i++) {
        const values = lines[i].split(",");

        const productName = values[1]; // Game Name column
        const sales = parseFloat(values[values.length - 1]);

        if (isNaN(sales)) continue;

        if (!productTotals[productName]) {
            productTotals[productName] = 0;
        }

        productTotals[productName] += sales;
        grandTotal += sales;
    }

    const productCount = Object.keys(productTotals).length;
    const datasetAverage = grandTotal / productCount;

    console.log("=== TOTAL SALES PER PRODUCT ===");
    for (let product in productTotals) {
        console.log(product + ": " + productTotals[product].toFixed(2));
    }

    console.log("\nDataset Average: " + datasetAverage.toFixed(2));

    console.log("\n=== LOW PERFORMING PRODUCTS ===");
    for (let product in productTotals) {
        if (productTotals[product] < datasetAverage) {
            console.log(product + " (Below Average)");
        }
    }

    rl.close();
}

// Start program
startProgram();