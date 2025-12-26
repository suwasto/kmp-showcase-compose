# Kotlin Multiplatform Library for Onboarding Showcases

A Kotlin Multiplatform library for creating onboarding showcases, UI highlights, and guided walkthroughs in Compose. Use it to spotlight key UI components, guide users step-by-step, and deliver polished intro experiences on both Android and iOS with simple, declarative APIs.

# Key Features:
- Multiplatform Support: Write your showcase logic once in commonMain and run it on both Android and iOS.
- Declarative API: Define showcase steps with simple composables. Use the .captureBounds() modifier to easily target any UI element.•Customizable Highlights: Control the look of your highlighted area with customizable shapes (rounded rectangle, circle) and styles.
- Flexible Content: Display any composable content for each step—not just a simple tooltip. Use Column, Button, or any other composable to create rich, interactive guides.
- Intelligent Tooltip Positioning: The built-in Tooltip automatically repositions itself to avoid screen edges, ensuring your content is always visible.
- Showcase Controller: A simple ShowcaseController to easily start and manage the sequence of your showcase steps.

## Demo
![Showcase-Compose](assets/sample.png)

## Installation

First, add the dependency to your commonMain source set in your build.gradle.kts file:

```kotlin
// in your commonMain dependencies
sourceSets {
    val commonMain by getting {
        dependencies {
            implementation("io.github.suwasto:kmp-showcase-compose:0.1.0") // Use latest version
        }
    }
}
```

For a complete example, check out the [composeApp](composeApp/src/commonMain/kotlin/io/github/suwasto/showcase) directory in the repository, which contains the usage of UI components and utilities for showcase onboarding.

### Step 1: Set up `ShowcaseHost` and Controller

First, wrap your screen's content with `ShowcaseHost` and create a `ShowcaseController` to manage the showcase.

```kotlin
@Composable
fun MyScreen() {
    val showcaseController = rememberShowcaseController()

    ShowcaseHost(controller = showcaseController) {
        // The rest of your screen's UI, e.g., a Scaffold
    }
}
```

### Step 2: Capture Component Bounds

To highlight a UI element, you need to capture its layout bounds. The `captureBounds` modifier makes this easy. It’s best to use a map to store the bounds (`Rect`) of each component you want to highlight.

```kotlin
// An enum to uniquely identify each highlight
enum class ShowcaseHighlight {
    Search, Bag
}

// Inside your composable
val showcaseLayouts = remember { mutableStateMapOf<ShowcaseHighlight, Rect>() }

// Apply the modifier to a component
IconButton(
    modifier = Modifier.captureBounds { rect ->
        showcaseLayouts[ShowcaseHighlight.Search] = rect
    },
    onClick = { /* ... */ }
) {
    Icon(Icons.Default.Search, contentDescription = "Search")
}
```

### Step 3: Define `ShowcaseStep`s

Once you have the bounds, you can define the steps for your showcase. A `ShowcaseStep` describes a single highlighted element and its accompanying content. The content can be any composable, not just a `Tooltip`. Using `derivedStateOf` ensures that your steps update automatically when the layout bounds are resolved. 

Define the steps inside your main composable so you can access the `showcaseController` if needed (e.g., for a "Next" button).

```kotlin
val showcaseSteps by remember {
    derivedStateOf {
        listOfNotNull(
            showcaseLayouts[ShowcaseHighlight.Search]?.let { rect ->
                ShowcaseStep(
                    targetRect = rect,
                    content = { SearchTooltip(showcaseController) }
                )
            },
            showcaseLayouts[ShowcaseHighlight.BAG]?.let { rect ->
                ShowcaseStep(
                    targetRect = rect,
                    content = { BagTooltip(showcaseController) }
                )
            }
        )
    }
}
```

### Step 4: Start the Showcase

Finally, use a `LaunchedEffect` to start the showcase once the steps are defined. To ensure the showcase runs only once, you can use a `rememberSaveable` flag.

```kotlin
// Start the sequence when steps are ready and on your condition
LaunchedEffect(showcaseSteps, shouldShowOnboardingShowcases) {
    if (shouldShowOnboardingShowcases) {
        showcaseController.start(showcaseSteps)
    }
}
```

### Putting It All Together

Here’s a complete example that combines all the steps into a single, self-contained composable.

```kotlin
// An enum to uniquely identify each highlight
enum class ShowcaseHighlight {
    Search, Bag
}

@Composable
fun MyScreenWithShowcase() {
    val showcaseController = rememberShowcaseController()

    // 1. A map to hold the layout bounds of the components to be highlighted.
    val showcaseLayouts = remember { mutableStateMapOf<ShowcaseHighlight, Rect>() }

    // 2. Define the showcase steps.
    // This is a derived state, so it will automatically update when showcaseLayouts changes.
    val showcaseSteps by remember {
        derivedStateOf {
            listOfNotNull(
                showcaseLayouts[ShowcaseHighlight.Search]?.let { rect ->
                    ShowcaseStep(
                        targetRect = rect,
                        content = { SearchTooltip(showcaseController) }
                    )
                },
                showcaseLayouts[ShowcaseHighlight.BAG]?.let { rect ->
                    ShowcaseStep(
                        targetRect = rect,
                        content = { BagTooltip(showcaseController) }
                    )
                }
            )
        }
    }

    // 3. Use a LaunchedEffect to start the showcase.
    // rememberSaveable is used to ensure the showcase runs only once.
    var shouldShowShowcase by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(shouldShowShowcase) {
        if (shouldShowShowcase) {
            showcaseController.start(showcaseSteps)
        }
    }

    // 4. Wrap your UI with ShowcaseHost and use the captureBounds modifier.
    ShowcaseHost(controller = showcaseController) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("My App") },
                    actions = {
                        IconButton(
                            modifier = Modifier.captureBounds { rect ->
                                showcaseLayouts[ShowcaseHighlight.Search] = rect
                            },
                            onClick = { /* ... */ }
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.captureBounds { rect ->
                        showcaseLayouts[ShowcaseHighlight.Bag] = rect
                    },
                    onClick = { /* ... */ }
                ) {
                    Icon(Icons.Default.ShoppingBag, contentDescription = "Bag")
                }
            }
        ) { paddingValues ->
            // Main content
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                Text(
                    text = "Content of the screen",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
```

### Customizing the Tooltip
Inside the content block of a ShowcaseStep, you can design any Compose UI. You have access to the controller to move to the next step or dismiss the tour.

#### Standar Onboarding Showcase Sample
```kotlin
fun getSwocaseStepSearch(
    rect: Rect,
    showcaseController: ShowcaseController
): ShowcaseStep {

    var showcaseStyle: ShowcaseStyle = ShowcaseStyle.Standard(shape = ShowcaseShape.Circle)
    var dimColor: Color = Color.Black.copy(alpha = 0.7f)

    return ShowcaseStep(
        style = showcaseStyle,
        rect = rect,
        dimColor = dimColor,
        onClickHighlight = {},
        content = { highlightRect ->
            Tooltip(
                anchorRect = highlightRect,
                direction = TooltipDirection.Bottom,
                bubbleStyle = TooltipBubbleStyle(
                    bubblePaddingStart = 8.dp,
                    bubblePaddingEnd = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text("Let's find what you need.", fontSize = 24.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Whenever you’re looking for something, this is your go-to spot. Try searching for \"Shoes\" or \"Jacket\" to see how it works.")
                    Spacer(modifier = Modifier.height(24.dp))
                    Row {
                        Text("1/2")
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            "Next",
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable {
                                showcaseController.next()
                            }
                        )
                    }
                }
            }
        },
    )
}
```
![ios_1.gif](assets/ios_1.gif)

#### Animated Overlay Showcase Onboarding Sample

```kotlin
fun getSwocaseStepSearch(
    rect: Rect,
    showcaseController: ShowcaseController
): ShowcaseStep {

    var showcaseStyle: ShowcaseStyle = ShowcaseStyle.Standard(shape = ShowcaseShape.Circle)
    var dimColor: Color = Color.Black.copy(alpha = 0.7f)

    return ShowcaseStep(
        style = showcaseStyle,
        rect = rect,
        enableDimAnim = true, // dim anim enabled
        dimColor = dimColor,
        onClickHighlight = {},
        content = { highlightRect ->
            // same content with previous sample
        },
    )
}
```
![ios_2.gif](assets/ios_2.gif)

#### Water Ripple Cutout Animation

```kotlin
fun getSwocaseStepSearch(
    rect: Rect,
    showcaseController: ShowcaseController
): ShowcaseStep {
    // showcasestyle waterdrop ripple
    var showcaseStyle: ShowcaseStyle = ShowcaseStyle.WaterDropRipple(color = Color.Cyan)
    var dimColor: Color = Color.Black.copy(alpha = 0.7f)

    return ShowcaseStep(
        style = showcaseStyle,
        rect = rect,
        enableDimAnim = true, // dim anim enabled
        dimColor = dimColor,
        onClickHighlight = {},
        content = { highlightRect ->
              var animateContentVisibility by rememberSaveable { mutableStateOf(false) }
              LaunchedEffect(Unit) {
                  delay(600)
                  animateContentVisibility = true
              }
              AnimatedVisibility(
                  animateContentVisibility,
                  enter = fadeIn() + scaleIn(initialScale = 0.8f)
              ) {
                  Box(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                      Card(
                          modifier = Modifier.align(Alignment.Center),
                          colors = CardDefaults.cardColors(containerColor = Color.White)
                      ) {
                          Column(
                              modifier = Modifier
                                  .padding(16.dp)
                          ) {
                              Text("Let's find what you need.", fontSize = 24.sp)
                              Spacer(modifier = Modifier.height(16.dp))
                              Text("Whenever you’re looking for something, this is your go-to spot. Try searching for \"Shoes\" or \"Jacket\" to see how it works.")
                              Spacer(modifier = Modifier.height(24.dp))
                              Row {
                                  Text("1/2")
                                  Spacer(modifier = Modifier.weight(1f))
                                  Button(
                                      modifier = Modifier.wrapContentWidth(),
                                      onClick = {
                                          showcaseController.next()
                                      }
                                  ) {
                                      Text(
                                          "Next"
                                      )
                                  }
                              }
                          }
                      }
                  }
              }
        },
    )
}
```
![ios_3.gif](assets/ios_3.gif)

#### Pulsing Circle Cutout Animation

```kotlin
fun getSwocaseStepSearch(
    rect: Rect,
    showcaseController: ShowcaseController
): ShowcaseStep {
    // showcasestyle pulsing circle
    var showcaseStyle: ShowcaseStyle = ShowcaseStyle.PulsingCircle(color = Color.Cyan)
    var dimColor: Color = Color.Black.copy(alpha = 0.7f)

    return ShowcaseStep(
        style = showcaseStyle,
        rect = rect,
        enableDimAnim = true, // dim anim enabled
        dimColor = dimColor,
        onClickHighlight = {},
        content = { highlightRect ->
            // same content with previous sample
        },
    )
}
```
![ios_4.gif](assets/ios_4.gif)

### `ShowcaseStep` Parameters

| Parameter | Description |
|---|---|
| `rect` | The `Rect` of the component to highlight. |
| `style` | The style of the showcase highlight. See `ShowcaseStyle`. |
| `highlightPadding` | Padding to apply to the highlighted area. |
| `onClickHighlight` | A lambda to be invoked when the highlighted area is clicked. |
| `enableDimAnim` | Whether to animate the dim background. |
| `dimAnimationDurationMillis` | The duration of the dim animation. |
| `dimColor` | The color of the dim background. |
| `content` | The content to display for this step. This can be any composable and receives the `highlightRect` as a parameter. |

### `Tooltip` Parameters

| Parameter | Description |
|---|---|
| `anchorRect` | The `Rect` of the component to which the tooltip is anchored. |
| `direction` | The direction in which the tooltip should be displayed relative to the anchor. See `TooltipDirection`. |
| `bubbleStyle` | The style of the tooltip bubble. See `TooltipBubbleStyle`. |
| `content` | The content to display inside the tooltip. |

### 📄 License

```
Copyright 2025 Anang Suwasto

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```