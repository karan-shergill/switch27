# Job Scheduler

# 2nd Oct 2026

https://www.hellointerview.com/practice/system-design/cmuqs8plh31a801adegzs2f7q

![2-10-2026_job_scheduler](images/2-10-2026_job_scheduler.png)

## Feedback

1. **Requirements**
    1. High availability is a core requirement for any job scheduler. If the scheduler goes down, jobs miss their scheduled times entirely. Always include HA as a requirement and note that job creation can be eventually consistent, meaning you favor availability over strict consistency for new jobs.
    2. At-least-once execution means every scheduled job must run at least once even if failures occur, so no job is ever silently dropped. This is the fundamental reliability guarantee of a scheduler and should be stated clearly as a non-functional requirement.
2. **Core Entities**
    1. When designing a job scheduler, separate the job definition from its execution history. A Scheduled Job stores what should run and when, while an Executed Job (or Job Run) stores the result of each individual execution. This separation lets you retry failed jobs, track history, and query past runs without polluting the job definition table.
3. **High Level Design**
    1. When designing a job scheduler, the simplest mental model to anchor on is three components: a database that stores job definitions, a scheduler process that polls for due jobs, and workers that execute them and write results back. Starting here before adding complexity like CDC or message queues makes your design easier to reason about and easier for interviewers to follow.
    2. For recurring jobs, be explicit about whether you pre-create one next execution row or many future rows. Pre-creating only the next execution row is simpler and avoids unbounded row growth. After a job runs, the worker or scheduler creates the next execution row based on the cron schedule. ⭐️
    3. In a monitoring design, explicitly state who writes the status fields and when. Workers should update the execution row status when a run finishes, and the job table should store a summary status like last run result or next scheduled time. Saying this directly closes the loop on how the data stays fresh without needing extra components.
4. **Deep Dives**
    1. To size a worker fleet for a job scheduler, bucket upcoming executions into small time windows (like 1 minute), find the peak bucket count, then divide by average job duration to estimate peak concurrency.
    2. When designing autoscaling rules, name a concrete trigger threshold (like a target number of in-flight messages per worker), add a sustained-breach period before scaling up to avoid reacting to short spikes, and always scale out fast but scale in slowly to avoid thrashing.
        1. Each worker has a bounded concurrency, and we autoscale the worker fleet based on queue depth, message age and worker resource utilization. Therefore, 10,000 concurrent executions can be distributed across many worker instances
    3. Workers in a scalable job execution system should run as auto-scaling containers (like ECS or Kubernetes pods) or as serverless functions. This matters because containers give you fast horizontal scale-out and better resource packing when you need thousands of concurrent jobs.
    4. Set the SQS visibility timeout to be longer than the expected maximum job runtime. If the timeout is too short, a healthy long-running job will become visible again and get picked up by a second worker, causing duplicate execution.
    5. Kafka doesn't have SQS-style visibility timeout. I would use manual offset commits and commit only after successful execution. If the worker dies before committing, the consumer group rebalances and another worker will consume the record from the last committed offset. For long-running jobs, I would maintain a lease/heartbeat in the execution database rather than relying solely on Kafka's consumer timeout. An expired lease marks the execution eligible for retry. Since this provides at-least-once execution, jobs need idempotency using the execution ID. ⭐️
  

