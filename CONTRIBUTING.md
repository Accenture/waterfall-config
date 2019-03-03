# Contributing

> Hi!
> Thanks for taking the time to go through this document. By doing so you will ensure an effortless and fruitful contribution process.

When contributing to this project, please first discuss the change you want to make via [issue](https://github.com/Accenture/waterfall-config/issues) or start a conversation with the [owners of the project](https://github.com/orgs/Accenture/teams/waterfall-config-team). That will let them understand the intention of the contribution, and in turn they will be able to give you detailed advice about how to proceed.

Please note that we have a [code of conduct](./CODE_OF_CONDUCT.md), please follow it in all your interactions with the project.

# Pull Request Process

All contributions should be made through a [Pull Request Process](https://help.github.com/en/articles/about-pull-requests).

The contributions should follow these general guidelines:
+ **Validate that the new code/functionalities are generic in nature** &mdash; the design decision behind the library was to support a wide variety of use cases and we intend to keep it that way. If your contribution is simply solving your specific problem at hand it is less likely that it will be integrated.
+ **Justify the functionality of the newly added code** &mdash; before submitting a Pull Request, confirm that the functionality provided is not already covered, and that it has not been evaluated before.
+ **Verify that the code is defect free and does not affect the stability of the entire project** &mdash; it is very important to validate that the newly added code does not break the existing functionality by verifying that the existent set of unit tests are still working.
+ **Follow the same coding standards you find in the existing codebase** &mdash; while we are not especially *picky* about code standardization, we want to enforce a certain degree of uniformity in the codebase. The best way to understand the rules is to browse the existing source code.
+ **Pull Requests must update the existing documentation (if needed) and version of the library using the [Semantic Versioning Scheme](https://semver.org/)**

Pull Requests will be merged once you get the sign-off from the project's team. Note that contributions will be added using the [squash and merge](https://help.github.com/en/articles/about-merge-methods-on-github#squashing-your-merge-commits) option. This ensures that commits from your contribution are combined into one single commit, which helps up keeping master branch's history meaningful. Make sure that you rebase your contribution before presenting the Pull Request.