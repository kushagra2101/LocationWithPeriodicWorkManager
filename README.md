# LocationWithPeriodicWorkManager

- Created a periodic work (repetitive task/job) using the Work Manager that runs every 1 hour.
- When the work starts, fetch the location using FLP (Fused Location Provider).
- Saved the timestamp, lat long information to the Room database in the background through Coroutines.
- Shown the lat long, date time in a RecyclerView and Paging on the Home screen.
