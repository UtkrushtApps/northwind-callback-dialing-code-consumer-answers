# Solution Steps

1. Use the provided fetch seam instead of calling the HTTP API directly from the formatting function. In Python call fetch_country, in Go call Fetch, in Java call the injected Fetcher, and in JavaScript call module.exports.fetchCountry so Jest can replace the export.

2. Read the returned JSON/model response and locate the data collection. Treat a missing, null, non-array, or empty data collection as a not-found country result.

3. If no country record exists, immediately return the exact literal string "-1".

4. For a found country, use the first country record from the data array, then read its callingCodes array/list.

5. Select the calling code at the highest index, which is the last element of the callingCodes array/list, instead of always using the first element.

6. Build the formatted callback number by concatenating a plus sign, the selected calling code, one space, and the original phoneNumber unchanged.

7. Add small defensive checks for missing or empty callingCodes to avoid panics/crashes; returning "-1" is a safe fallback for malformed service data.

8. Run the language-specific offline tests: pytest in python, go test in go, mvn test in java, or npm test in javascript. The tests pass because they stub the fetch seam rather than reaching the network.

