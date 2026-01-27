const fs = require("fs");
const { parse } = require("csv-parse");
const axios = require("axios");

const CSV_PATH = __dirname + "/sign_data.csv";
const API_URL = "http://localhost:8080/api/observations";

const delay = (ms) => new Promise(r => setTimeout(r, ms));

let count = 0;

const stream = fs.createReadStream(CSV_PATH).pipe(
  parse({
    delimiter: ",",
    trim: true,
    relax_column_count: true,
  })
);

stream.on("data", async (row) => {
  stream.pause();

  if (count >= 5) {
    console.log("Stopping after 10 rows");
    stream.destroy(); 
    return;
  }
  if (count === 1) {
  console.log("FIRST ROW FROM CSV:", row);
}


  count++;

  const observation = {
    latitude: Number(row[0]),
    longitude: Number(row[1]),
    heading: Number(row[2]),
    type: row[3],
    speed: row[4] ? Number(row[4]) : null,
  };

  try {
    await axios.post(API_URL, observation);
    console.log(`Sent row ${count}`);
    await delay(300);
  } catch (err) {
    console.error(
      "POST failed:",
      err.response?.status,
      err.response?.data || err.message
    );
  }

  stream.resume();
});

stream.on("end", () => {
  console.log("CSV fully processed");
});

stream.on("error", (err) => {
  console.error("CSV error:", err.message);
});
