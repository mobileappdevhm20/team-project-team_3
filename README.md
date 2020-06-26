# Recify
You don't know what to cook and have no time? Recify provides YOU with the best suggestion for your ingredients from all over the internet.
## Contents
* [Concept](#concept)  
* [Architecture](#architecture)  
* [Devevelopment Process](#devprocess) 
* [Learnings](#learnings) 
* [Demo](#demo) 
* [Links](#links) 
 
<a name="concept"/>

## Concept
The base concept of our app is that you dont need to break your head over finding a suitable recipe for you current state of ingredients. Just type in what you have and get the perfect recipe for your needs. And in addition to that you have nice features like automatic timer inside the instructions and the ability to check a step and have a good overview of what still needs to be done.
### Storyboard
![Storyboard](https://github.com/mobileappdevhm20/team-project-team_3/raw/develop/docs/recipefinder.png "Storyboard")
### Prototype
[Recify App Prototype](https://www.figma.com/proto/6evHuOysZnhX9llKyLIDB6/Recify?node-id=1%3A8&scaling=scale-down)

<a name="architecture"/>

## Architecture
### Components
![Components](https://raw.githubusercontent.com/mobileappdevhm20/team-project-team_3/gh-pages/docs/components.png "Components")
### Project Structure
```
team3.recipefinder/
├── activity/
│   // Side activity classes
├── adapter/
│   // Custom adapters
├── dao/
│   // Database daos with all custom queries
├── database/
│   // Database table models
├── dialog/
│   // Dialogframents
├── listener/
│   // Custom listerners
├── logic/
│   // Custom logic that is not util related
│   ├── crawl/
│   │   // Custom logic for the import crawler
│   └── search/
│       // Custom logic for the search recipe feature
├── model/
│   // Data entities for the database and crawler object mapping
├── service/
│   // Services to communicate with the main activity from an detached thread
├── ui/
│   // Views for custom features
├── util/
│   // Basic static util classes
├── viewModel/
│   // ViewModels for different database accesses
├── viewModelFactory/
│   // Factory to create specified viewModels
└── MainActivity.kt
```

<a name="devprocess"/>

## Devevelopment Process
![Timeline](https://raw.githubusercontent.com/mobileappdevhm20/team-project-team_3/gh-pages/docs/timeline.png "Timeline")
### Feature States
<style>
.tablelines table, .tablelines td, .tablelines th {
        border: 1px solid black;
        }
</style>

| Features | Sprint 1 | Sprint 2 | Final State |
| ------ | ------ | ------ | ------ |
| User Management | Done | Done | Done |
| Create / Edit Recipes | Done | Done | Done |
| Read Recipes / Start Timers | Done | Done | Done |
| Search Recipe | Planned | Done | Done |
| Import Recipes | Planned | Done | Done |
| Scale Recipe | Planned | Done | Done |
| Cookbook Management | In Progress | In Progress | Done |
| Share Recipe | Planned | Additional Feature | Additional Feature |
{: .tablelines}
<a name="learnings"/>

## Learnings

<a name="demo"/>

## Demo

<iframe width="560" height="315" src="https://www.youtube.com/embed/6IMdTPjsvWQ" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>

<a name="links"/>

## Links
### Sprint 2 - Presentation
* [Presentation](https://docs.google.com/presentation/d/1J1FMQb8dLY3UUmCycBY6zfTF4ZKT6R_gjU5TsndktBA/edit?usp=sharing)
### Privacy Policy and Terms of Condition
* [Privacy Policy](privacy.html)
* [Terms & Conditions](termsOfService.html)

