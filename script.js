let data = [];
let regions = new Set();

// Load CSV automatically
fetch("data.csv")
  .then(res => res.text())
  .then(text => {
    data = parseCSV(text);
    buildRegionDropdown();
  });

function parseCSV(text) {
  const lines = text.trim().split("\n");
  const headers = lines[0].split(",");

  return lines.slice(1).map(line => {
    const values = line.split(",");
    let obj = {};

    headers.forEach((h, i) => {
      let val = values[i];
      obj[h] = val === "null" ? null : isNaN(val) ? val : Number(val);
    });

    return obj;
  });
}

function buildRegionDropdown() {
  const select = document.getElementById("regionSelect");

  data.forEach(row => regions.add(row.region));
  regions = [...regions].sort();

  regions.forEach(region => {
    let option = document.createElement("option");
    option.value = region;
    option.textContent = region;
    select.appendChild(option);
  });
}

function getRegionData() {
  const region = document.getElementById("regionSelect").value;
  return data.filter(row => row.region === region);
}

function runHighestLowest() {
  const regionData = getRegionData();

  let highest = null;
  let lowest = null;

  regionData.forEach(country => {
    const speeds = extractSpeeds(country);
    if (speeds.length === 0) return;

    const max = Math.max(...speeds);
    const min = Math.min(...speeds);

    if (!highest || max > highest.speed)
      highest = { name: country.country, speed: max };

    if (!lowest || min < lowest.speed)
      lowest = { name: country.country, speed: min };
  });

  show(`
    🔵 <b>Highest Speed:</b> ${highest.name} — <b>${highest.speed} Mbps</b><br>
    🔴 <b>Lowest Speed:</b> ${lowest.name} — <b>${lowest.speed} Mbps</b>
  `);
}

function runImprovement() {
  const regionData = getRegionData();

  let best = null;

  regionData.forEach(country => {
    const speeds = extractSpeeds(country);
    if (speeds.length < 2) return;

    const first = speeds[0];
    const last = speeds[speeds.length - 1];
    const diff = last - first;

    if (!best || diff > best.diff)
      best = { name: country.country, diff };
  });

  show(`
    📈 <b>Biggest Improvement:</b> ${best.name}<br>
    Gained <b>${best.diff.toFixed(2)} Mbps</b>
  `);
}

function runInequality() {
  const regionData = getRegionData();

  let allSpeeds = [];

  regionData.forEach(country => {
    allSpeeds.push(...extractSpeeds(country));
  });

  const max = Math.max(...allSpeeds);
  const min = Math.min(...allSpeeds);
  const range = max - min;

  show(`
    ⚡ <b>Digital Inequality Index:</b> ${range.toFixed(2)} Mbps<br>
    (Range between highest and lowest)
  `);
}

function extractSpeeds(country) {
  return Object.keys(country)
    .filter(k => k.startsWith("year"))
    .map(k => country[k])
    .filter(v => typeof v === "number");
}

function show(html) {
  document.getElementById("results").innerHTML = html;
}
