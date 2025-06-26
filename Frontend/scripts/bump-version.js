const fs = require("fs");
const path = require("path");

const packagePath = path.resolve(__dirname, "../package.json");
const pkg = require(packagePath);

// 🔼 Bump patch version (e.g., 1.0.0 → 1.0.1)
let [major, minor, patch] = pkg.version.split(".").map(Number);
patch++;
pkg.version = `${major}.${minor}.${patch}`;

fs.writeFileSync(packagePath, JSON.stringify(pkg, null, 2));

// Optionally, create a version.ts file for Angular
const versionFile = path.resolve(__dirname, "../src/environments/version.ts");
fs.writeFileSync(
  versionFile,
  `// Auto-generated version file\nexport const appVersion = '${pkg.version}';\n`
);

console.log(`✅ Version bumped to ${pkg.version}`);
