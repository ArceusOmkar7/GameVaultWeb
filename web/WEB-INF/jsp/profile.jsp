<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Profile - GameVault</title>
    <script src="https://cdn.tailwindcss.com"></script>
  </head>
  <body class="bg-gray-100">
    <jsp:include page="header.jsp" />

    <div class="container mx-auto px-4 py-8">
      <%-- Messages --%>
      <c:if test="${not empty message}">
        <div
          class="mb-4 p-4 rounded ${messageType eq 'success' ? 'bg-green-100 border border-green-400 text-green-700' : 'bg-red-100 border border-red-400 text-red-700'}"
          role="alert"
        >
          <p><c:out value="${message}" /></p>
        </div>
      </c:if>

      <div
        class="max-w-2xl mx-auto bg-white rounded-lg shadow-md overflow-hidden"
      >
        <div class="p-6">
          <div class="flex justify-between items-start mb-6">
            <h1 class="text-2xl font-bold text-gray-900">Profile</h1>
            <a
              href="${pageContext.request.contextPath}/editProfile"
              class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
            >
              Edit Profile
            </a>
          </div>

          <div class="space-y-4">
            <div>
              <p class="text-sm font-medium text-gray-500">Username</p>
              <p class="mt-1 text-lg text-gray-900">
                <c:out value="${user.username}" />
              </p>
            </div>

            <div>
              <p class="text-sm font-medium text-gray-500">Email</p>
              <p class="mt-1 text-lg text-gray-900">
                <c:out value="${user.email}" />
              </p>
            </div>

            <div class="pt-4 border-t">
              <div class="flex justify-between items-center">
                <div>
                  <p class="text-sm font-medium text-gray-500">
                    Wallet Balance
                  </p>
                  <p class="mt-1 text-2xl font-bold text-gray-900">
                    $<fmt:formatNumber
                      value="${user.walletBalance}"
                      pattern="#,##0.00"
                    />
                  </p>
                </div>
                <button
                  onclick="document.getElementById('addBalanceForm').classList.toggle('hidden')"
                  class="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
                >
                  Add Balance
                </button>
              </div>

              <!-- Add Balance Form (Hidden by default) -->
              <form
                id="addBalanceForm"
                action="${pageContext.request.contextPath}/addBalance"
                method="post"
                class="hidden mt-4 p-4 bg-gray-50 rounded"
              >
                <div class="flex gap-4">
                  <div class="flex-grow">
                    <label
                      for="amount"
                      class="block text-sm font-medium text-gray-700"
                      >Amount to Add ($)</label
                    >
                    <input
                      type="number"
                      id="amount"
                      name="amount"
                      min="0.01"
                      step="0.01"
                      required
                      class="mt-1 block w-full rounded border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                    />
                  </div>
                  <div class="flex items-end">
                    <button
                      type="submit"
                      class="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
                    >
                      Add
                    </button>
                  </div>
                </div>
              </form>
            </div>

            <div class="pt-4 border-t">
              <p class="text-sm font-medium text-gray-500">Account Created</p>
              <p class="mt-1 text-lg text-gray-900">
                <fmt:formatDate
                  value="${user.createdAt}"
                  pattern="MMMM d, yyyy"
                />
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <jsp:include page="footer.jsp" />
  </body>
</html>
